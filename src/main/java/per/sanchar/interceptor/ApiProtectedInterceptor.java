package per.sanchar.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import per.sanchar.annotation.AccessLimit;
import per.sanchar.util.MemoryCacheUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.OutputStream;
import java.util.Objects;

/**
 * description:
 *
 * @author shencai.huang@hand-china.com
 * @date 2021/9/12 5:08 下午
 * lastUpdateBy: shencai.huang@hand-china.com
 * lastUpdateDate: 2021/9/12
 */
@Component
public class ApiProtectedInterceptor implements HandlerInterceptor {

    public static final String ACCESS_LIMIT_REACHED = "ACCESS_LIMIT_REACHED";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        //判断请求是否属于方法的请求
        if (handler instanceof HandlerMethod) {
            HandlerMethod hm = (HandlerMethod) handler;
            AccessLimit accessLimit = hm.getMethodAnnotation(AccessLimit.class);
            if (Objects.isNull(accessLimit)) {
                return true;
            }
            int seconds = accessLimit.seconds();
            int maxCount = accessLimit.maxCount();
            boolean needLogin = accessLimit.needLogin();
            String key = request.getRequestURI();
            // 如果需要登录
            if (needLogin) {
                // 获取登录的session进行判断
                // ......
                //这里假设用户是1,项目中是动态获取的userId
                key += "1";
            }
            // 从memory cache中获取用户访问的次数，项目中建议用redis或者其它缓存数据库
            Integer count = MemoryCacheUtils.getData(key);
            if (Objects.isNull(count)) {
                // 第一次访问
                MemoryCacheUtils.setData(key, 1, seconds * 1000);
            } else if (count < maxCount) {
                MemoryCacheUtils.setData(key, count + 1, seconds * 1000);
            } else {
                //超出访问次数
                render(response, ACCESS_LIMIT_REACHED);
                return false;
            }
        }
        return true;
    }

    private void render(HttpServletResponse response, String msg) throws Exception {
        response.setContentType("application/json;charset=UTF-8");
        OutputStream out = response.getOutputStream();
        out.write(msg.getBytes("UTF-8"));
        out.flush();
        out.close();
    }
}

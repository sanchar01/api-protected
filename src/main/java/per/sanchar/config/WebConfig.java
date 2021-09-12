package per.sanchar.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import per.sanchar.interceptor.ApiProtectedInterceptor;

import javax.annotation.Resource;

/**
 * description:
 *
 * @author shencai.huang@hand-china.com
 * @date 2021/9/12 5:49 下午
 * lastUpdateBy: shencai.huang@hand-china.com
 * lastUpdateDate: 2021/9/12
 */
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {

    @Resource
    private ApiProtectedInterceptor apiProtectedInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(apiProtectedInterceptor);
    }


}

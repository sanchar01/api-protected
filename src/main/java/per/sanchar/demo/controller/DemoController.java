package per.sanchar.demo.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import per.sanchar.annotation.AccessLimit;

/**
 * description:
 *
 * @author shencai.huang@hand-china.com
 * @date 2021/9/12 5:54 下午
 * lastUpdateBy: shencai.huang@hand-china.com
 * lastUpdateDate: 2021/9/12
 */
@RestController
public class DemoController {

    @AccessLimit(seconds = 5, maxCount = 5, needLogin = true)
    @RequestMapping("/test")
    public String test() {
        return "success";
    }
}

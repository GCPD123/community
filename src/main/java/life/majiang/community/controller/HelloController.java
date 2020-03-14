package life.majiang.community.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * @author Xue
 * @date 2020/3/13 - 9:08 下午
 */
@Controller
public class HelloController {
    @GetMapping("/")
    public String hello(){

        return "index";
    }
}

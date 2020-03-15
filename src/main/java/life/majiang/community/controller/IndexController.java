package life.majiang.community.controller;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/13 - 9:08 下午
 */
@Controller
public class IndexController {
    @Autowired
    UserMapper userMapper;
    @GetMapping("/")
    public String index(HttpServletRequest request){
        Cookie[] cookies = request.getCookies();
        if(cookies != null){
            for (Cookie cookie : cookies) {
                if(cookie.getName().equals("token")){
                    String token = cookie.getValue();
                    User user =  userMapper.findToken(token);
                    if(user !=null){
                        //找到了用户 还是放到session中 方便从前端取出来
                        request.getSession().setAttribute("user",user);
                    }
                    break;
                }
            }
        }



        return "index";
    }
}

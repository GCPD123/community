package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.User;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @author Xue
 * @date 2020/3/13 - 9:08 下午
 */
@Controller
public class IndexController {
    @Autowired
    QuestionService questionService;
    @Autowired
    UserMapper userMapper;

    @GetMapping("/")
    public String index(HttpServletRequest request, Model model,
                        @RequestParam(value = "page", defaultValue = "1") Integer page,
                        @RequestParam(value = "size", defaultValue = "5") Integer size) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    User user = userMapper.findToken(token);
                    if (user != null) {
                        //找到了用户 还是放到session中 方便从前端取出来 是为了让页面取出信息 ${session}
                        //cookie是为了让浏览器记住登陆状态
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //service层的来源 当一个类需要组装两个部分时 question+user 就需要用到中间层来组装 现在还加上分页

        PaginationDTO pagination = questionService.list(page,size);
        //不仅带有每个问题的信息 还有用户的所有信息
        model.addAttribute("pagination", pagination);

        return "index";
    }
}

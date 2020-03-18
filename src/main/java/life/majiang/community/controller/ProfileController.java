package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import life.majiang.community.service.QuestionService;
import org.h2.store.Page;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/17 - 8:20 下午
 */
@Controller
public class ProfileController {
    @Autowired
    UserMapper userMapper;
    @Autowired
    QuestionService questionService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action, Model model,
                          HttpServletRequest request,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "5") Integer size){
//还是验证是否登陆
        //提升作用域
        User user = null ;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                     user = userMapper.findToken(token);
                    if (user != null) {
                        //找到了用户 还是放到session中 方便从前端取出来 是为了让页面取出信息 ${session}
                        //cookie是为了让浏览器记住登陆状态
                        request.getSession().setAttribute("user", user);
                    }
                    break;
                }
            }
        }
        //未登陆 直接到首页
        if(user == null){
            return "redirect:/";
        }

        if(action.equals("question")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
        }else if (action.equals("reply")){
            model.addAttribute("section","reply");
            model.addAttribute("sectionName","我的回复");
        }

        //利用service层查询出和用户关联的问题列表 依然是要分页的 所以要这些
        PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
        //仍然传给页面
        model.addAttribute("pagination",paginationDTO);
        return "profile";
    }
}

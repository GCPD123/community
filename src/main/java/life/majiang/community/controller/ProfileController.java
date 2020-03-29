package life.majiang.community.controller;

import life.majiang.community.dto.PaginationDTO;
import life.majiang.community.modle.User;
import life.majiang.community.service.NotificationService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/17 - 8:20 下午
 */
@Controller
public class ProfileController {

    @Autowired
    QuestionService questionService;

    @Autowired
    private NotificationService notificationService;

    @GetMapping("/profile/{action}")
    public String profile(@PathVariable("action") String action, Model model,
                          HttpServletRequest request,
                          @RequestParam(value = "page", defaultValue = "1") Integer page,
                          @RequestParam(value = "size", defaultValue = "5") Integer size){
//还是验证是否登陆 拦截器已经将用户存入seesion了 现在要验证直接取出来即可


        User user = (User) request.getSession().getAttribute("user");
        //未登陆 直接到首页
        if(user == null){
            return "redirect:/";
        }

        if(action.equals("question")){
            model.addAttribute("section","question");
            model.addAttribute("sectionName","我的提问");
            //利用service层查询出和用户关联的问题列表 依然是要分页的 所以要这些
            PaginationDTO paginationDTO = questionService.list(user.getId(), page, size);
            //仍然传给页面
            model.addAttribute("pagination",paginationDTO);
        }else if (action.equals("reply")){
            PaginationDTO paginationDTO = notificationService.list(user.getId(), page, size);
            Long unreadCount = notificationService.unreadCount(user.getId());
            model.addAttribute("pagination",paginationDTO);
            model.addAttribute("section","reply");
            model.addAttribute("sectionName","我的回复");
        }


        return "profile";
    }
}

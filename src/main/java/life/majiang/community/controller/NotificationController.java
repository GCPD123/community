package life.majiang.community.controller;

import life.majiang.community.dto.NotificationDTO;
import life.majiang.community.enums.NotificationTypeEnum;
import life.majiang.community.modle.User;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/27 - 3:23 下午
 */
@Controller
public class NotificationController {
    @Autowired
    private NotificationService notificationService;
    @GetMapping("/notification/{id}")
    public String notification(@PathVariable("id") Long id, HttpServletRequest request){
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            return "redirect:/";
        }
        //读取 很具消息的id来取
       NotificationDTO notificationDTO =  notificationService.read(id,user);

        if(NotificationTypeEnum.REPLY_COMMENT.getType() == notificationDTO.getType() ||
        NotificationTypeEnum.REPLY_QUESTION.getType() == notificationDTO.getType()){

            //重定向到相关问题的页面
            return "redirect:/question/"+notificationDTO.getOuterid();
        }else {
            return "redirect:/";
        }

    }
}

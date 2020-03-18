package life.majiang.community.controller;


import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/16 - 10:43 上午
 */
@Controller
public class PublishController {

    @Autowired
    QuestionMapper questionMapper;



    @GetMapping("/publish")
    public String publish(){
        return "publish";
    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("desc")String desc,
                            @RequestParam("tag") String tag,
                            HttpServletRequest request,
                            Model model){

        //页面回显数据
        model.addAttribute("title",title);
        model.addAttribute("desc",desc);
        model.addAttribute("tag",tag);

        //数据校验
        if(title == null || title ==""){
            model.addAttribute("error","标题不能为空");
            return "publish";
        }if(desc == null || desc ==""){
            model.addAttribute("error","描述不能为空");
            return "publish";
        }if(tag == null || tag ==""){
            model.addAttribute("error","标签不能为空");
            return "publish";
        }


//从seesion中找用户
        User user = (User) request.getSession().getAttribute("user");
        //未登陆不能提交
        if(user == null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        Question quesion = new Question();
        quesion.setTitle(title);
        quesion.setDesc(desc);
        quesion.setTag(tag);
        quesion.setCreator(user.getId());
        quesion.setGmtCreate(System.currentTimeMillis());
        quesion.setGmtModified(quesion.getGmtCreate());
        questionMapper.create(quesion);

        return "redirect:/";
    }
}

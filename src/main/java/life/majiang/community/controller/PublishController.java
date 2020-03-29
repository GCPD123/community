package life.majiang.community.controller;


import life.majiang.community.cache.TagCache;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.dto.TagDTO;
import life.majiang.community.mapper.QuestionMapper;
import life.majiang.community.modle.Question;
import life.majiang.community.modle.User;
import life.majiang.community.service.QuestionService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/16 - 10:43 上午
 */
@Controller
public class PublishController {

//    @Autowired
//    QuestionMapper questionMapper;
    @Autowired
    QuestionService questionService;



    @GetMapping("/publish/{id}")
    public String edit(@PathVariable("id") Long id,Model model){
        //只是多一个封装 原数据不变
        QuestionDTO question = questionService.getById(id);
        //因为是页面要求的这样一个一个传
        model.addAttribute("title",question.getTitle());
        model.addAttribute("desc",question.getDesc());
        model.addAttribute("tag",question.getTag());
        //给页面一个id 后面页面又将id传回后台 好判断
        model.addAttribute("id",question.getId());
        //将准备好的可选标签给页面 展示出来
        model.addAttribute("tags", TagCache.get());

        //修改和发布页面是同一个 但是后面根据是否有问题id可以进行修改和新增
        return "publish";

    }

    //该方法是复用 修改和新增一起 所以要判断 根据id判断
    @GetMapping("/publish")
    public String publish(Model model){
        //将准备好的可选标签给页面 展示出来
        model.addAttribute("tags", TagCache.get());
        return "publish";

    }
    @PostMapping("/publish")
    public String doPublish(@RequestParam("title") String title,
                            @RequestParam("desc")String desc,
                            @RequestParam("tag") String tag,
                            @RequestParam(value = "id",required = false) Long id,
                            HttpServletRequest request,
                            Model model){

        //页面回显数据
        model.addAttribute("title",title);
        model.addAttribute("desc",desc);
        model.addAttribute("tag",tag);
        //将准备好的可选标签给页面 展示出来
        model.addAttribute("tags", TagCache.get());

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

        String invalid = TagCache.filterInvalid(tag);
        if(StringUtils.isNotBlank(invalid)){
            model.addAttribute("error","输入非法标签"+invalid);
            return "publish";
        }


//从seesion中找用户
        User user = (User) request.getSession().getAttribute("user");
        //未登陆不能提交
        if(user == null){
            model.addAttribute("error","用户未登陆");
            return "publish";
        }
        Question question = new Question();
        question.setTitle(title);
        question.setDesc(desc);
        question.setTag(tag);
        question.setCreator(user.getId());

        question.setId(id);
        //新增或更新 由id判断
        questionService.createOrUpdate(question);


//        questionMapper.create(question);

        return "redirect:/";
    }

}

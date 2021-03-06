package life.majiang.community.controller;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.QuestionDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.service.CommentService;
import life.majiang.community.service.QuestionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

/**
 * @author Xue
 * @date 2020/3/18 - 7:05 下午
 */
@Controller
public class QuestionController {
    @Autowired
    QuestionService questionService;
//    为了拿到回复的列表
    @Autowired
    CommentService commentService;

    @GetMapping("/question/{id}")
    public String question(@PathVariable("id") Long id, Model model){
        //里面有用户对象 更好的封装 但是需要用service处理封装 是由于需要获得的对象有更高的封装 所以需要中间层处理
        QuestionDTO questionDTO = questionService.getById(id);
        //获取所有相关问题 通过标签查找 将这个问题的相关问题都取出来
        List<QuestionDTO> relatedQuestion = questionService.selectRelated(questionDTO);
        //根据问题的id取出来回复的列表 这个是要传递到页面上面的 倒叙排序
        List<CommentDTO> commentDTO = commentService.listByTargetId(id, CommentTypeEnum.QUESTION);
        //增加阅读数
        questionService.incView(id);
        //将问题和回复 还有相关问题都放到页面上
        model.addAttribute("question",questionDTO);
        model.addAttribute("comment",commentDTO);
        model.addAttribute("relatedQuestion",relatedQuestion);
        return "question";
    }


}

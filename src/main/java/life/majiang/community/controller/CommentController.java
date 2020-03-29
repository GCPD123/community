package life.majiang.community.controller;

import life.majiang.community.dto.CommentCreateDTO;
import life.majiang.community.dto.CommentDTO;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.modle.Comment;
import life.majiang.community.modle.User;
import life.majiang.community.service.CommentService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.util.List;


/**
 * @author Xue
 * @date 2020/3/20 - 7:55 下午
 */
@Controller
public class CommentController {


    @Autowired
    CommentService commentService;

    @ResponseBody
    @RequestMapping(value = "/comment",method = RequestMethod.POST)
    public Object post(@RequestBody CommentCreateDTO commentDTO, HttpServletRequest request ){
        //拿到用户信息 都在session中
        User user = (User) request.getSession().getAttribute("user");
        if(user == null){
            //不使用异常，也可以使用结果类来传达 只要有code和message的类就可以
            return ResultDTO.errorOf(CustomizeErrorCode.NO_LOGIN);
        }
        //其他校验在service里面 使用工具类 只用判断一次 ""和null都判断了
        if(commentDTO == null || StringUtils.isBlank(commentDTO.getContent())){
            return ResultDTO.errorOf(CustomizeErrorCode.COMMENT_IS_EMPTY);
        }
        Comment comment = new Comment();
        comment.setParentId(commentDTO.getParentId());
        comment.setContent(commentDTO.getContent());
        comment.setType(commentDTO.getType());
        comment.setGmtCreate(System.currentTimeMillis());
        comment.setGmtModified(System.currentTimeMillis());
        comment.setCommentator(user.getId());
        //因为又要查评论表又要查问题表 所以定义一个中间层可以同时进行
        commentService.insert(comment,user);
        //成功后返回给页面
        return ResultDTO.okOf();
    }

    //获取二级列表 传入问题的id可以获取一级评论，传入一级评论id根据type判断后可以获取二级评论
    @ResponseBody
    @RequestMapping(value = "/comment/{id}",method = RequestMethod.GET)
    //返回值就会直接到list里面
    public ResultDTO<List<CommentDTO>> comments(@PathVariable("id") Long id){//先拿到一级评论的id
        //在service层抽取了方法，判断传入的类型，对于该方法来说传入的都是long类型的id一样 无非是type
        List<CommentDTO> commentDTOS = commentService.listByTargetId(id, CommentTypeEnum.COMMENT);
        return ResultDTO.okOf(commentDTOS);
    }
}

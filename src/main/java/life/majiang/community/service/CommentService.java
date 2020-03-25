package life.majiang.community.service;

import life.majiang.community.dto.CommentDTO;
import life.majiang.community.enums.CommentTypeEnum;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.mapper.*;
import life.majiang.community.modle.*;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @author Xue
 * @date 2020/3/20 - 8:38 下午
 */
@Service
public class CommentService {
    @Autowired
    CommentMapper commentMapper;
    @Autowired
    QuestionMapper questionMapper;
    @Autowired
    QuestionExtMapper questionExtMapper;
    @Autowired
    UserMapper userMapper;
    @Autowired
    CommentExtMapper commentExtMapper;

    //加上事务管理
    @Transactional
    public void insert(Comment comment) {
        //判断问题是否存在 但是现在啊已经在里面的层了 返回值只能给上层 不方便处理 所以可以用异常 直接到异常处理
        if (comment.getParentId() == null || comment.getParentId() == 0) {
            //实际上最核心的就是创建一个自定义的异常然后将其中的code和message赋值 然后该异常就会到达我们定义的异常处理类
            throw new CustomizeException(CustomizeErrorCode.TARGET_PARENT_NOT_FOUND);
        }
        if (comment.getType() == null || !CommentTypeEnum.isExist(comment.getType())) {
            throw new CustomizeException(CustomizeErrorCode.TYPE_PARAM_WRONG);
        }
        //CommentTypeEnum.COMMENT表示调用构造方法 创建一个CommentTypeEnum对象并 为type赋值，所以还需要get获取type的值
        if (comment.getType() == CommentTypeEnum.COMMENT.getType()) {
            //回复的是评论 先把该评论找出来 使用程序来控制parentid指向question表的key
            Comment dbComment = commentMapper.selectByPrimaryKey(comment.getParentId());
            if (dbComment == null) {
                throw new CustomizeException(CustomizeErrorCode.COMMENT_NOT_FOUND);
            }
            //存入
            commentMapper.insertSelective(comment);

            //这里要新创建一个对象 插入的时候只需要id和增长数
            Comment parentComment = new Comment();
            //增加的是从数据库中查出来的1级评论个数
            parentComment.setId(dbComment.getId());
            parentComment.setCommentCount(1);
            //让评论数增加
            commentExtMapper.incComment(parentComment);
        } else {
            //回复的是问题 先把问题找出来 后面要操作这个问题相关的属性
            Question question = questionMapper.selectByPrimaryKey(comment.getParentId());
            if(question == null){
                throw new CustomizeException(CustomizeErrorCode.QUESTION_NOT_FOUND);
            }
            //存入
            //这里存在事务 要一起完成或失败
            commentMapper.insertSelective(comment);
            //然后将该问题的评论数加1
            question.setCommentCount(1);
            questionExtMapper.incComment(question);
        }
    }

    public List<CommentDTO> listByTargetId(Long id, CommentTypeEnum type) {
        //这个id是问题的id 对应到评论表中就是parentID但是还需要type
        CommentExample example = new CommentExample();
        //这里CommentTypeEnum.QUESTION只是创建了一个CommentTypeEnum的对象 要type还要取出来 如果已经是commenttypeenum对象的话 那就直接get
        example.createCriteria().andParentIdEqualTo(id).andTypeEqualTo(type.getType());
        //想要取出来的东西倒叙排序 desc倒叙的意思 这里面是数据库语法
        example.setOrderByClause("gmt_create desc");
        //在该问题下的所有回复
        List<Comment> comments = commentMapper.selectByExample(example);
        if(comments.size() == 0){
            //返回一个空
            return new ArrayList<>();
        }


        //java8新语法 比如有10个评论 按照以前就要遍历10次 但是可能只有5个人没人评论2次，所以只要获取这5个人就可以
        Set<Long> commentators = comments.stream().map(comment -> comment.getCommentator()).collect(Collectors.toSet());
        //创建一个list的user集合方便下面传进去 集合中保存的是评论人的id
        ArrayList<Long> userIds = new ArrayList<>();
        userIds.addAll(commentators);

        //获取评论人转换为map
        UserExample example1 = new UserExample();
        //这是需要一个list
        example1.createCriteria().andIdIn(userIds);
        //拿到所有的user了
        List<User> users = userMapper.selectByExample(example1);
        //将回复和用户对应起来
        //将user转换成map 后面遍历的时候可以一次取出来
        Map<Long, User> userMap = users.stream().collect(Collectors.toMap(user -> user.getId(), user -> user));


        //将comment也变成list 转换comment为commentDTO传回页面
        List<CommentDTO> commentDTOS = comments.stream().map(comment -> {
            CommentDTO commentDTO = new CommentDTO();
            BeanUtils.copyProperties(comment,commentDTO);
            //这里就是取出usermap里面的value也就是user对象 提高了效率
            commentDTO.setUser(userMap.get(comment.getCommentator()));
            return commentDTO;
        }).collect(Collectors.toList());
        return commentDTOS;
    }
}

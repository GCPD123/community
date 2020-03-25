package life.majiang.community.dto;

import life.majiang.community.modle.User;
import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/22 - 12:58 下午
 */
@Data
public class CommentDTO {
    private Long id;
    private Long parentId;
    private Integer type;
    private Long commentator;
    private Long gmtCreate;
    private Long gmtModified;
    private Long likeCount;
    private String content;
    private Integer commentCount;
    //需要评论的用户信息
    private User user;
}

package life.majiang.community.dto;

import lombok.Data;

/** 这个是页面传过来 存储评论的 不是我们传到页面上的
 * @author Xue
 * @date 2020/3/20 - 7:58 下午
 */
@Data
public class CommentCreateDTO {
    private Long parentId;
    private String content;
    private Integer type;
}

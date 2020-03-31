package life.majiang.community.dto;

import life.majiang.community.modle.User;
import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/16 - 6:51 下午
 */
@Data
public class QuestionDTO {
    private Long id;
    private String title;
    private String description;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Long creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;
    //关联user可以获取头像
    private User user;
}

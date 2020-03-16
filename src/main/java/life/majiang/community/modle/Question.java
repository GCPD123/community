package life.majiang.community.modle;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/16 - 11:43 上午
 */
@Data
public class Question {
    private Integer id;
    private String title;
    private String desc;
    private String tag;
    private Long gmtCreate;
    private Long gmtModified;
    private Integer creator;
    private Integer viewCount;
    private Integer commentCount;
    private Integer likeCount;

}

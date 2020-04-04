package life.majiang.community.dto;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/28 - 8:39 下午
 */
@Data
public class QuestionQueryDTO {
    private String search;
    private String tag;
    private Integer page;
    private Integer size;
}

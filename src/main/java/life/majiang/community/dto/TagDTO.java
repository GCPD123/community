package life.majiang.community.dto;

import lombok.Data;

import java.util.List;

/**
 * @author Xue
 * @date 2020/3/25 - 7:11 下午
 */
@Data
public class TagDTO {
    private String categoryName;
    private List<String> tags;
}

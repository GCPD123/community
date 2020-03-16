package life.majiang.community.dto;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/14 - 5:55 下午
 */
@Data
public class GIthubUser {
    private String name;
    private Long id;
    private String bio;//描述
    private String avatar_url;


}

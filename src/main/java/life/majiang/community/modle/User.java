package life.majiang.community.modle;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/15 - 12:10 下午
 */
@Data
public class User {
    private Integer id;
    private String name;
    private String accountId;
    private String token;
    private Long gmtCreate;
    private Long gmtModified;
    private String avatarUrl;
}


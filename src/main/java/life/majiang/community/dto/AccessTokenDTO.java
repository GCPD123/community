package life.majiang.community.dto;

import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/14 - 5:06 下午
 * dto = data transfer object
 */
@Data
public class AccessTokenDTO {
    private String client_id;
    private String client_secret;
    private String code;
    private String redirect_uri;
    private String state;


}

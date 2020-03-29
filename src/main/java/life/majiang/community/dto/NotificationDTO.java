package life.majiang.community.dto;

import life.majiang.community.modle.User;
import lombok.Data;

/**
 * @author Xue
 * @date 2020/3/26 - 11:17 下午
 */
@Data
public class NotificationDTO {
    private Long id;
    private Long gmtCreate;
    private Integer status;
    private Long notifier;
    private String notifierName;
    //outer表示外层 也就是问题
    private String outerTitle;
    private Long outerid;
    private String typeName;
    private Integer type;
}

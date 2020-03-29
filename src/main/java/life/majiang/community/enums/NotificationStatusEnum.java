package life.majiang.community.enums;

/**
 * @author Xue
 * @date 2020/3/26 - 8:36 下午
 */
public enum NotificationStatusEnum {
   UNREAD(0),
   READ(1)


    ;
    private int status;

    NotificationStatusEnum(int status) {
        this.status = status;
    }

    public int getStatus() {
        return status;
    }
}

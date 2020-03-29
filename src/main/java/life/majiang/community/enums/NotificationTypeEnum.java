package life.majiang.community.enums;

/**
 * @author Xue
 * @date 2020/3/26 - 8:23 下午
 */
public enum NotificationTypeEnum {
    REPLY_QUESTION(1,"回复了问题"),
    REPLY_COMMENT(2,"回复了评论")

    ;


    private int type;
    private String name;

    NotificationTypeEnum(int status, String name) {
        this.type = status;
        this.name = name;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    //判断传入的类型是否在这个枚举中
    public static String nameOfType(int type){
        for (NotificationTypeEnum value : NotificationTypeEnum.values()) {
            if(value.getType() == type){
                return value.getName();
            }
        }
        return "";
    }
}

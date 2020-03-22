package life.majiang.community.enums;

/** 该类可以定义type的值 可以获得
 * @author Xue
 * @date 2020/3/20 - 8:34 下午
 */
public enum CommentTypeEnum {
    QUESTION(1),
    COMMENT(2);
    private Integer type;

    CommentTypeEnum(Integer type) {
        this.type = type;
    }

    //判断传来的类型是否在这个枚举里面
    public static boolean isExist(Integer type) {

        for (CommentTypeEnum value : CommentTypeEnum.values()) {
            if(value.getType() == type){
                return true;
            }
        }
        return false;
    }

    public Integer getType() {
        return type;
    }
}

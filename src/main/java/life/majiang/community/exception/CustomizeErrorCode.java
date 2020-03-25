package life.majiang.community.exception;

/** 该类是对错误状态的封装
 * @author Xue
 * @date 2020/3/19 - 9:46 下午
 */
public enum CustomizeErrorCode implements ICustomizeErrorCode {
//枚举类型 也就是当前类中的所有属性都需要 访问的时候可以通过类名访问 实际上是使用的构造来创建这个枚举
    //相当于调用这个就调用了构造方法 也就是给类中的属性赋值了
    QUESTION_NOT_FOUND(2001,"你找的问题不在了，要不换个试试？"),
    TARGET_PARENT_NOT_FOUND(2002,"未选中任何问题或评论进行回复"),
    NO_LOGIN(2003,"当前操作需要登陆，请登陆后再重试"),
    SYS_ERROR(2004,"服务器冒烟了，稍后再试试"),
    TYPE_PARAM_WRONG(2005,"评论类型错误或不存在"),
    COMMENT_NOT_FOUND(2006,"你找的评论不在了，要不换个试试？"),
    COMMENT_IS_EMPTY(2007,"输入内容不能为空！")

;


    private String message;
    private Integer code;

    //构造两个属性
    CustomizeErrorCode(Integer code, String message) {
        this.message = message;
        this.code = code;
    }

    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public Integer getCode() {
        return code;
    }
}

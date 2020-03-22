package life.majiang.community.exception;

/**
 * @author Xue
 * @date 2020/3/19 - 6:41 下午
 */
public class CustomizeException extends RuntimeException{
    private String message;
    //加上一个状态码
    private Integer code;
//异常一定要有一个有参构造方法和message 构造的时候就把状态码和异常信息传进去
    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        this.message=customizeErrorCode.getMessage();
        this.code = customizeErrorCode.getCode();

    }

    //这个方法是实现的方法，一样是get and setter
    @Override
    public String getMessage() {
        return message;
    }

    public Integer getCode() {
        return code;
    }
}

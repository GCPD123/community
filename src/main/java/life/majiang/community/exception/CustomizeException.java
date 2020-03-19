package life.majiang.community.exception;

/**
 * @author Xue
 * @date 2020/3/19 - 6:41 下午
 */
public class CustomizeException extends RuntimeException{
    private String message;
//异常一定要有一个有参构造方法和message
    public CustomizeException(ICustomizeErrorCode customizeErrorCode) {
        this.message=customizeErrorCode.getMessage();

    }

    @Override
    public String getMessage() {
        return message;
    }
}

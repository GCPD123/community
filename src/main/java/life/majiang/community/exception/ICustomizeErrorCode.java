package life.majiang.community.exception;

/**
 * @author Xue
 * @date 2020/3/19 - 9:44 下午
 */
public interface ICustomizeErrorCode {
    //只有一个方法 具体的属性在实现类里面 只是规定要使用这个方法名
     String getMessage();
     Integer getCode();
}

package life.majiang.community.dto;

import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import lombok.Data;

/**
 * 该类是为了统一异常处理时候的返回内容
 *
 * @author Xue
 * @date 2020/3/20 - 8:25 下午
 */
@Data
//T代表一切
public class ResultDTO<T> {
    //返回的状态码 前端可根据这个判断 是都显示
    private Integer code;
    private String message;
    private T data;

    //其他地方直接可以用 传入两个参数直接创建出响应属性的对象
    public static ResultDTO errorOf(Integer code, String message) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(code);
        resultDTO.setMessage(message);
        return resultDTO;
    }

    //可以用上面的构造 来给code 和message赋值可以传入异常代码类
    public static ResultDTO errorOf(CustomizeErrorCode errorCode) {

        return ResultDTO.errorOf(errorCode.getCode(), errorCode.getMessage());

    }
    //定义的异常类 可以传如异常类
    public static ResultDTO errorOf(CustomizeException ex) {
        return errorOf(ex.getCode(),ex.getMessage());
    }

    //成功后返回的
    public static ResultDTO okOf() {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        return resultDTO;
    }

    public static <T> ResultDTO okOf(T t) {
        ResultDTO resultDTO = new ResultDTO();
        resultDTO.setCode(200);
        resultDTO.setMessage("请求成功");
        resultDTO.setData(t);
        return resultDTO;
    }

}


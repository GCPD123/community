package life.majiang.community.advice;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.ResultDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/** 需要页面请求的异常以页面展示，因为在一个方法中要返回两种形式，这里直接暴力写到页面上
 * json请求就返回json
 * @author Xue
 * @date 2020/3/19 - 6:08 下午
 */
//异常是指我的服务器处理后但是出错了 但是请求的资源没有的话是没有异常类型的 因为我们没规定所以就无法处理
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    Object handle(Throwable ex, Model model, HttpServletRequest request, HttpServletResponse response) {
        //获取contenttype 是为了判断返回json还是网页 不通的请求返回的不一样 因为safari浏览器没有content type
        //所以用accept也可以做判断 意思希望接受的数据类型
        String accept = request.getHeader("Accept");
        String[] strings = accept.split(",");
        for (String string : strings) {
            //返回json 不需要页面
            if (string.equals("application/json")) {
                ResultDTO resultDTO;
                //因为已经判断是否属于该类 所以方法参数可以缩小范围
                if (ex instanceof CustomizeException) {
                    //这里还是利用了返回结果中的code和message方法
                    resultDTO = ResultDTO.errorOf((CustomizeException) ex);
                } else {
                    resultDTO = ResultDTO.errorOf(CustomizeErrorCode.SYS_ERROR);

                }
                //暴力以json写到页面上
                try {
                    response.setContentType("application/json");
                    response.setStatus(200);
                    response.setCharacterEncoding("utf-8");
                    PrintWriter writer = response.getWriter();
                    //将这个对象转换成json 对象中只有code和message ,所以和json格式一样
                    writer.write(JSON.toJSONString(resultDTO));
                    writer.close();
                } catch (IOException e) {

                }
                //因为已经写到页面上了，所以不需要返回值
                return null;
            } else {
                //返回页面
                //我自定义的异常
                if (ex instanceof CustomizeException) {
                    model.addAttribute("message", ex.getMessage());
                } else {
                    //不认识的异常类
                    //这里只需要message所以不要code
                    model.addAttribute("message", CustomizeErrorCode.SYS_ERROR.getMessage());
                }
                return new ModelAndView("error");
            }
        }
            //这里只是为了不报错，因为前面所有的判断里面都有返回的处理了，但是都在for里面，所以需要在外层处理返回
            return  null;
    }

}

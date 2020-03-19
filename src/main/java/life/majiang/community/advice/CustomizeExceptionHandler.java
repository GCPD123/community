package life.majiang.community.advice;

import life.majiang.community.exception.CustomizeException;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Xue
 * @date 2020/3/19 - 6:08 下午
 */
//异常是指我的服务器处理后但是出错了 但是请求的资源没有的话是没有异常类型的 因为我们没规定所以就无法处理
@ControllerAdvice
public class CustomizeExceptionHandler {

    @ExceptionHandler(Exception.class)
    ModelAndView handle( Throwable ex, Model model){
        //我自定义的异常
        if(ex instanceof CustomizeException){
            model.addAttribute("message",ex.getMessage());
        }else {

            model.addAttribute("message","服务器爆了，要不稍后在试试！！！");
        }
        return new ModelAndView("error");
    }

}

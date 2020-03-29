package life.majiang.community.interceptor;

import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import life.majiang.community.modle.UserExample;
import life.majiang.community.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;

/**
 * @author Xue
 * @date 2020/3/18 - 1:54 下午
 */
//拦截器必须要实现这个接口 使用autowired必须要是spring中的bean 所以要注入容器 但是注入后就不能new这个类了
@Component
public class SessionInterceptor implements HandlerInterceptor {
    @Autowired
    UserMapper userMapper;
    @Autowired
    NotificationService notificationService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if (cookie.getName().equals("token")) {
                    String token = cookie.getValue();
                    UserExample example = new UserExample();
                    //拼接sql
                    example.createCriteria().andTokenEqualTo(token);
                    List<User> users = userMapper.selectByExample(example);
//                    User user = userMapper.findToken(token);
                    if (users.size() != 0) {
                        //找到了用户 还是放到session中 方便从前端取出来 是为了让页面取出信息 ${session}
                        //cookie是为了让浏览器记住登陆状态
                        request.getSession().setAttribute("user", users.get(0));
                        //获取当前用户未读读条数
                        Long unreadCount = notificationService.unreadCount(users.get(0).getId());
                        request.getSession().setAttribute("unreadCount",unreadCount);
                    }
                    break;
                }
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}

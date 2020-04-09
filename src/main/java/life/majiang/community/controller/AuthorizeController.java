package life.majiang.community.controller;

import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GIthubUser;
import life.majiang.community.mapper.UserMapper;
import life.majiang.community.modle.User;
import life.majiang.community.provider.GithubProvider;
import life.majiang.community.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.UUID;

/**
 * @author Xue
 * @date 2020/3/14 - 4:59 下午
 */
@Controller
//lombok插件的主角 可以直接将日志类注入进来 可以直接用
@Slf4j
public class AuthorizeController {
    @Autowired
    GithubProvider githubProvider;

    @Value("${github.client.id}")
    private String clientId;

    @Value("${github.client.secret}")
    private String clientSecret;

    @Value("${github.redirect.uri}")
    private String redirectUri;


    @Autowired
    UserService userService;

    @GetMapping("/callback")
    public String callback(@RequestParam(name = "code") String code,
                           @RequestParam(name = "state") String state,
                           HttpServletResponse response) {
        System.out.println(redirectUri);
        AccessTokenDTO accessTokenDTO = new AccessTokenDTO();
        log.info("redirectUri,{}",redirectUri);
        accessTokenDTO.setRedirect_uri(redirectUri);
        accessTokenDTO.setClient_id(clientId);
        accessTokenDTO.setClient_secret(clientSecret);
        accessTokenDTO.setCode(code);
        accessTokenDTO.setState(state);
        String accessToken = githubProvider.getAccessToken(accessTokenDTO);
        log.info("accessToken {}",accessToken);
        //这个是github到用户信息
        GIthubUser githubUser = githubProvider.getUser(accessToken);
        if (githubUser != null) {
            //将github给的用户模型转换成我们数据库需要的
            User user = new User();
            String token = UUID.randomUUID().toString();
            user.setToken(token);
            user.setName(githubUser.getName());
            user.setAccountId(String.valueOf(githubUser.getId()));

            user.setAvatarUrl(githubUser.getAvatar_url());
//          唯一的是accountid是gitgub给的
            userService.createOrUpdate(user);

            //这里如果没有上面的判断则每次都会无脑插入一个新用户 即使是同一个用户
//            userMapper.insert(user);
            //登陆成功写入数据库 用数据库存储信息 然后给浏览器一个token 是我们定义的 只有我们知道 然后凭这个token就可以去数据库查找用户是否在
            response.addCookie(new Cookie("token", token));
//            //这时手动写一个固定的cokkie 为了测试
//            request.getSession().setAttribute("githubUser",githubUser);
            //重定向到当前站点 默认是首页
            System.out.println(githubUser.getName());
            return "redirect:/";
        } else {
//            输出日志 将后面的内容输出到{}里面
            log.error("callback get github error,{}",githubUser);
            //登陆失败
            return "redirect:/";
        }


    }

    @GetMapping("/logout")
    public String logout(HttpServletRequest request, HttpServletResponse response) {
        //首先移除session
        request.getSession().removeAttribute("user");
        //再移除cookie
        Cookie cookie = new Cookie("token", null);
        cookie.setMaxAge(0);
        response.addCookie(cookie);
        return "redirect:/";
    }
}

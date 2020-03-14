package life.majiang.community.provider;

import com.alibaba.fastjson.JSON;
import life.majiang.community.dto.AccessTokenDTO;
import life.majiang.community.dto.GIthubUser;
import okhttp3.*;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**用来发送post请求 获得tonken 专门与github打交道的类
 * @author Xue
 * @date 2020/3/14 - 5:05 下午
 */
@Component
public class GithubProvider {

    /**
     * 根据参数获取access_token 按照api的要求发送相应的参数就可获得
     * @param accessTokenDTO 封装的参数
     * @return
     */
    public String getAccessToken(AccessTokenDTO accessTokenDTO){
         MediaType mediaType = MediaType.get("application/json; charset=utf-8");

        OkHttpClient client = new OkHttpClient();
        RequestBody body = RequestBody.create(mediaType,JSON.toJSONString(accessTokenDTO) );
        Request request = new Request.Builder()
                .url("https://github.com/login/oauth/access_token")
                .post(body)
                .build();
        try (Response response = client.newCall(request).execute()) {
            String string = response.body().string();
            System.out.println(string);
            String[] split = string.split("&");
            String s = split[0];
            String[] split1 = s.split("=");
            String token = split1[1];//这里拿到的就是纯token
            return token;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public GIthubUser getUser(String accessToken){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder()
                .url("https://api.github.com/user?access_token="+accessToken)
                .build();
        try {
            Response response = client.newCall(request).execute();
            String string = response.body().string();
            GIthubUser gIthubUser = JSON.parseObject(string, GIthubUser.class);
            return gIthubUser;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }


}

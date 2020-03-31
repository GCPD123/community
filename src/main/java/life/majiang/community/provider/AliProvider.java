package life.majiang.community.provider;

import com.aliyun.oss.OSS;
import com.aliyun.oss.OSSClientBuilder;
import com.aliyun.oss.model.PutObjectRequest;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.net.URL;
import java.util.Date;
import java.util.UUID;

/**
 * @author Xue
 * @date 2020/3/28 - 4:14 下午
 */
@Service
public class AliProvider {
    @Value("${aliyun.oss.endpoint}")
    private String endpoint;
    @Value("${aliyun.oss.accessKeyId}")
    private String accessKeyId;
    @Value("${aliyun.oss.accessKeySecret}")
    private String accessKeySecret;
    @Value("${aliyun.oss.bucketName}")
    private String bucketName;
    @Value("${aliyun.oss.expire}")
    private Long expire;


    public String upload( InputStream inputStream,String fileName){
//        这里需要转译 不然.是正则的匹配符号
        String[] split = fileName.split("\\.");
        String generatedFileName = "";
        if(split.length > 1){
//            //分割的最后一位 也就是扩展名
            generatedFileName = UUID.randomUUID().toString() + "." + split[split.length - 1];
        }else {
            return null;
        }
        // 创建OSSClient实例。
        OSS ossClient = new OSSClientBuilder().build(endpoint, accessKeyId,accessKeySecret);

        // 创建PutObjectRequest对象。 该方法支持文件流
        ossClient.putObject(bucketName, generatedFileName, inputStream);

        // 设置URL过期时间为1小时。
        Date expiration = new Date(new Date().getTime() +  expire);
        // 生成以GET方法访问的签名URL，访客可以直接通过浏览器访问相关内容。 页面上查看时 可以显示图片
        URL url = ossClient.generatePresignedUrl(bucketName, generatedFileName, expiration);


        // 关闭OSSClient。
        ossClient.shutdown();

        return url.toString();

    }

}

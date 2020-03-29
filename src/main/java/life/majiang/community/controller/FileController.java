package life.majiang.community.controller;

import life.majiang.community.dto.FileDTO;
import life.majiang.community.exception.CustomizeErrorCode;
import life.majiang.community.exception.CustomizeException;
import life.majiang.community.provider.AliProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import java.io.IOException;

/**
 * @author Xue
 * @date 2020/3/28 - 12:34 下午
 */
@Controller
public class FileController {

    //    存储到aliyun
    @Autowired
    private AliProvider aliProvider;

    @RequestMapping("/file/upload")
    @ResponseBody
//    要接受到前端传来的文件信息
    public FileDTO upload(MultipartHttpServletRequest request) {
//        获取指定的name的文件 前端提交的id
        MultipartFile file = request.getFile("editormd-image-file");
        FileDTO fileDTO = new FileDTO();
        try {
//            传的是源文件的名字 而不是封装它的名字
            String url = aliProvider.upload(file.getInputStream(), file.getOriginalFilename());
//        楼上是存储 下面是返回给页面让你知道上传成功
//            将图片存储的地址返回给页面 是md要求的返回内容
            fileDTO.setUrl(url);
            fileDTO.setSuccess(1);
        } catch (IOException e) {
            throw new CustomizeException(CustomizeErrorCode.FILE_UPLOAD_FALI);
        }

        return fileDTO;
    }
}

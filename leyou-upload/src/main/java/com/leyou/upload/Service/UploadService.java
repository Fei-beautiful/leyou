package com.leyou.upload.Service;

import com.github.tobato.fastdfs.domain.StorePath;
import com.github.tobato.fastdfs.domain.ThumbImageConfig;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;

@Service
public class UploadService {

    @Autowired
    private FastFileStorageClient storageClient;



   //支持的文件类型
    private static final List<String> CONTENT_TYPE = Arrays.asList("image/jpeg","image/png");
// 日志打印
    private static  final Logger LOGGER = LoggerFactory.getLogger(UploadService.class);
    public String uploadImage(MultipartFile file) {
       // 获取上传文件的原始名称
        String originalFilename = file.getOriginalFilename();

        try {
            //检查上传的文件类型
            //获取文件的媒体类型
            String contentType = file.getContentType();
            if (!CONTENT_TYPE.contains(contentType)){
                LOGGER.error("文件类型不合法!" +originalFilename);
                return  null;
            }


            //检查上传文件的内容
            BufferedImage bufferedImage = ImageIO.read(file.getInputStream());
            if (bufferedImage == null){
                LOGGER.info("文件内容不合法：{}"+originalFilename);
                return  null;
            }

           /* //保存到服务器
            file.transferTo(new File("F:\\adminLTE\\image"+originalFilename ));

            // 返回url， 进行回显
            return  "http://image.leyou.com/"+originalFilename;*/
           //通过storageClient把图片上传到fastDFS
            //获取图片后缀名
            String extension = StringUtils.substringAfterLast(file.getOriginalFilename(), ".");
            // 2.2、上传
            StorePath storePath = this.storageClient.uploadFile(
                    file.getInputStream(), file.getSize(), extension, null);
            //返回url路径
            return  "http://image.leyou.com/"+storePath.getFullPath();
        } catch (IOException e) {
            LOGGER.info("服务器累了。请稍后在使用！"+originalFilename);
            e.printStackTrace();
        }
        return  null;
    }
}

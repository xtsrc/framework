package com.xt.framework.oss.util;

import com.xt.framework.oss.core.MinioService;
import com.xt.framwork.common.core.util.QrCodeGenerateUtil;
import com.xt.framwork.common.core.util.UuidUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.entity.ContentType;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import java.io.ByteArrayInputStream;

/**
 * @author tao.xiong
 * @Description 文件工具包
 * @Date 2022/6/29 11:10
 */
@Slf4j
@Component
public class OssUtil {
    @Value("${spring.profiles.active}")
    private String profiles;
    /**
     * 二维码文件夹
     */
    private static final String FOLDER = "qrcode";
    @Resource
    private MinioService minioService;
    private static final String LINUX_SEPARATOR = "/";

    /**
     * 生成二维码并且上传
     *
     * @param text 二维码内容
     * @return url地址
     */
    public String generateQrCodeAndUpload(String text) throws Exception {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(QrCodeGenerateUtil.getQrCodeImage(text, 300, 300));
        return upload(inputStream, LINUX_SEPARATOR + profiles + FOLDER + LINUX_SEPARATOR + "qrc-" + UuidUtil.getUuid() + ".png",
                ContentType.IMAGE_PNG.getMimeType());
    }

    /**
     * 单个文件上传
     *
     * @param inputStream 文件流
     * @param fileName    bucket下的文件名，可以包含目录
     * @param contentType 文件类型
     * @return url地址
     */
    public String upload(ByteArrayInputStream inputStream, String fileName, String contentType) throws Exception {
        try {
            minioService.putObject(inputStream, fileName, contentType);
            return minioService.getFileUrl(fileName);
        } catch (Exception oe) {
            log.error("oss upload error:{}", oe.getMessage());
            throw oe;
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
        }
    }

}

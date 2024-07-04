package com.hzjy.hzjycheckIn.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.ResourceUtils;
import org.springframework.web.multipart.MultipartFile;
import java.io.*;
import java.util.UUID;

public class FileUtil {
    private final static Logger logger = LoggerFactory.getLogger(FileUtil.class);

    public static String uploadFile(MultipartFile multipartFile) throws Exception {
        String fileName = UUID.randomUUID().toString() + ".xls";
        String path = ResourceUtils.getURL("classpath:").getPath() + "static/upload/";
        File file = new File(path);
        if (!file.exists()) {
            file.mkdirs();
        }
        String fileFullPath = path + fileName;
        logger.info("fileFullPath:" + fileFullPath);
        InputStream inputStream = null;
        FileOutputStream fos = null;
        try {
            inputStream = multipartFile.getInputStream();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(inputStream);
            fos = new FileOutputStream(fileFullPath);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(fos);
            int size = multipartFile.getBytes().length;
            byte[] buffer = new byte[1024];// 一次读多个字节
            while ((size = inputStream.read(buffer, 0, buffer.length)) != -1) {
                fos.write(buffer, 0, buffer.length);
            }
        } catch (Exception ex) {
            logger.error("ex:" + ex.getMessage());
        } finally {
            if (inputStream != null) {
                inputStream.close();
            }
            if (fos != null) {
                fos.close();
            }
        }
        return fileFullPath;
    }
    /*
     * 获取文件扩展名 小写
     * */
    public static String getFileExt(String fileName) {
        String ext = "";
        try {
            if (!fileName.equals("")) {
                ext = fileName.substring(fileName.lastIndexOf(".")).toLowerCase();
            }
        } catch (Exception e) {
            ext = "";
        }
        return ext;
    }

}

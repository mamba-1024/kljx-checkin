// package com.hzjy.hzjycheckIn.controller.web;

// import com.aliyun.oss.OSS;
// import com.aliyun.oss.model.ObjectMetadata;
// import com.aliyun.oss.model.PutObjectResult;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.beans.factory.annotation.Value;
// import org.springframework.http.ResponseEntity;
// import org.springframework.stereotype.Controller;
// import org.springframework.web.bind.annotation.PostMapping;
// import org.springframework.web.bind.annotation.RequestParam;
// import org.springframework.web.multipart.MultipartFile;

// import java.io.IOException;
// import java.io.InputStream;
// import java.util.UUID;

// @Controller
// public class OSSController {
//     @Autowired
//     private OSS ossClient;

//     @Value("${oss.bucketName}")
//     private String bucketName;

//     @PostMapping("/upload")
//     public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) throws IOException {
//         String fileName = UUID.randomUUID().toString() + "-" + file.getOriginalFilename();
//         InputStream inputStream = file.getInputStream();
//         ObjectMetadata metadata = new ObjectMetadata();
//         metadata.setContentLength(file.getSize());
//         metadata.setContentType(file.getContentType());
//         PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, inputStream, metadata);
// //        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
// //        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis()+3600*1000));
// //        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
// //        System.out.printf(url.toString());
//         return ResponseEntity.ok(fileName);
//     }


//     @PostMapping("/wechat/upload")
//     public ResponseEntity<String> uploadWechatFile(@RequestParam("file") MultipartFile file) throws IOException {
//         String fileName = "auth/"+UUID.randomUUID() + "-" + file.getOriginalFilename();
//         InputStream inputStream = file.getInputStream();
//         ObjectMetadata metadata = new ObjectMetadata();
//         metadata.setContentLength(file.getSize());
//         metadata.setContentType(file.getContentType());
//         PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, inputStream, metadata);
// //        GeneratePresignedUrlRequest generatePresignedUrlRequest = new GeneratePresignedUrlRequest(bucketName, fileName);
// //        generatePresignedUrlRequest.setExpiration(new Date(System.currentTimeMillis()+3600*1000));
// //        URL url = ossClient.generatePresignedUrl(generatePresignedUrlRequest);
// //        System.out.printf(url.toString());
//         return ResponseEntity.ok(fileName);
//     }


//     @PostMapping("/backend/upload")
//     public ResponseEntity<String> uploadBackendFile(@RequestParam("file") MultipartFile file) throws IOException {
//         String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
//         InputStream inputStream = file.getInputStream();
//         ObjectMetadata metadata = new ObjectMetadata();
//         metadata.setContentLength(file.getSize());
//         metadata.setContentType(file.getContentType());
//         PutObjectResult putObjectResult = ossClient.putObject(bucketName, fileName, inputStream, metadata);
//         return ResponseEntity.ok(fileName);
//     }

// }


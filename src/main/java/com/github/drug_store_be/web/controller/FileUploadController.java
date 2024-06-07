package com.github.drug_store_be.web.controller;

import com.amazonaws.services.s3.AmazonS3Client;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
@RequestMapping("/upload")
@RequiredArgsConstructor
public class FileUploadController {
    private final AmazonS3Client amazonS3Client;

    @Value("${cloud.aws.s3.bucket-name}")
    private String bucket;

    @PostMapping("/pics")
    public ResponseDto uploadFile(@RequestParam("file") MultipartFile file){
        try{
            String fileName= file.getOriginalFilename();
            String fileUrl= "https://"+ bucket+ "/test"+ fileName;
            ObjectMetadata metadata= new ObjectMetadata();
            metadata.setContentType(file.getContentType());
            metadata.setContentLength(file.getSize());

            amazonS3Client.putObject(bucket, fileName, file.getInputStream(), metadata);
            return new ResponseDto(HttpStatus.OK.value(), "file upload success", fileUrl);
        } catch (IOException e) {
            e.printStackTrace();
            return new ResponseDto(HttpStatus.INTERNAL_SERVER_ERROR.value(), "upload fail");
        }
    }
}

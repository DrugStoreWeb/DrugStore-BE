package com.github.drug_store_be.service.service;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.AmazonS3Exception;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.drug_store_be.service.exceptions.StorageUpdateFailedException;
import com.github.drug_store_be.web.DTO.awsS3.FileDto;
import com.github.drug_store_be.web.DTO.awsS3.SaveFileType;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class StorageService {
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;
    private final AmazonS3 amazonS3Client;
    public List<FileDto> fileUploadAndGetUrl(List<MultipartFile> multipartFiles, SaveFileType type) {
        List<FileDto> response= new ArrayList<>();

        switch(type){
            case small:
                for(MultipartFile file: multipartFiles){
                    PutObjectRequest putObjectRequest= makePutObjectRequest(file);
                    amazonS3Client.putObject(putObjectRequest);
                    String url= amazonS3Client.getUrl(bucketName, putObjectRequest.getKey()).toString();
                    response.add(new FileDto(file.getOriginalFilename(), url));
                }
                break;
            case large:
                break;
        }
        return response;
    }

    private PutObjectRequest makePutObjectRequest(MultipartFile file) {
        String storageFileName= makeStorageFileName(Objects.requireNonNull(file.getOriginalFilename()));
        ObjectMetadata objectMetadata= new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try{
            return new PutObjectRequest(bucketName, storageFileName, file.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new StorageUpdateFailedException("File Upload Failed", file.getOriginalFilename());
        }
    }

    private String makeStorageFileName(String orignialFileName) {
        String extension= orignialFileName.substring(orignialFileName.lastIndexOf(".")+1);
        return UUID.randomUUID() + "." + extension;
    }


    public void uploadCancel(List<String> fileUrls) {
        try{
            for(String url: fileUrls){
                String[] parts= url.split("/");
                String key= parts[parts.length-1];
                amazonS3Client.deleteObject(bucketName, key);
            }
        }catch(AmazonS3Exception e){
            e.printStackTrace();
            throw new StorageUpdateFailedException("File Delete Failed "+ e.getMessage(), fileUrls.toString());
        }
    }
}

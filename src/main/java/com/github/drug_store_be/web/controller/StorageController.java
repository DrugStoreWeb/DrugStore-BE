package com.github.drug_store_be.web.controller;

import com.github.drug_store_be.service.service.StorageService;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.awsS3.FileDto;
import com.github.drug_store_be.web.DTO.awsS3.SaveFileType;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/storage")
@RequiredArgsConstructor
public class StorageController {
    private final StorageService storageService;

    //여러개 업로드
    @PostMapping("/multipart-files")
    public ResponseDto uploadMultipleFiles(@RequestPart("uploadFiles")List<MultipartFile> multipartFiles,
                                           @RequestParam(required= false) Optional<SaveFileType> type
    ){
        List<FileDto> response= storageService.fileUploadAndGetUrl(multipartFiles, type.orElseGet(()-> SaveFileType.small));
        return new ResponseDto(response);
    }

    //업로드 취소(삭제)
    @DeleteMapping("/multipart-files")
    public ResponseDto deleteMultipleFiles(@RequestParam(value= "file-url") List<String> fileUrls){
        storageService.uploadCancel(fileUrls);
        return new ResponseDto();
    }

    @PutMapping("/multipart-files")
    public ResponseDto modifyMultipleFiles(@RequestParam(value="file-url") List<String> deleteFileUrls,
                                           @RequestPart("uploadFiles") List<MultipartFile> multipartFiles,
                                           @RequestParam(required = false) Optional<SaveFileType> type){
        List<FileDto> response= storageService.fileUploadAndGetUrl(multipartFiles, type.orElseGet(()-> SaveFileType.small));
        storageService.uploadCancel(deleteFileUrls);
        return new ResponseDto(response);
    }
}

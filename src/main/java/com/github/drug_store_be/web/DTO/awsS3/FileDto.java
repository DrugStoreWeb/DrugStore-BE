package com.github.drug_store_be.web.DTO.awsS3;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class FileDto {
    private String fileName;
    private String fileUrl;
}

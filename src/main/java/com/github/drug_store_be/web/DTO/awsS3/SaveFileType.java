package com.github.drug_store_be.web.DTO.awsS3;

public enum SaveFileType {
    small("일반파일"),
    large("대용량파일");
    private final String kor;

    SaveFileType(String kor) {
        this.kor = kor;
    }
    public String getTypeKor(){
        return this.kor;
    }
}

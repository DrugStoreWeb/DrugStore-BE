package com.github.drug_store_be.web.DTO.Mypage;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoResponse {
    private String name;
    private String nickName;
    private String phoneNumber;
    private String profilePic;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
    private String address;
    private String email;
}

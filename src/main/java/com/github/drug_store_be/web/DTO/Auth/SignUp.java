package com.github.drug_store_be.web.DTO.Auth;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@Builder
public class SignUp {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String passwordCheck;
    private LocalDate birthday;
    private String phoneNumber;
    private String address;
    private String profilePic;

}

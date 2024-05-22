package com.github.drug_store_be.web.DTO.Auth;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString

@Builder
public class CheckResponse {
    private String message;
    private Boolean check;
}

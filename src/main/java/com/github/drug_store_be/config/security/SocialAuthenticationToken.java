package com.github.drug_store_be.config.security;

import lombok.Getter;
import org.springframework.security.authentication.AbstractAuthenticationToken;


public class SocialAuthenticationToken extends AbstractAuthenticationToken {
    private final String email;
    @Getter
    private final String kakaoUserId;

    public SocialAuthenticationToken(String email, String kakaoUserId) {
        super(null);
        this.email = email;
        this.kakaoUserId = kakaoUserId;
        setAuthenticated(false);
    }


    @Override
    public Object getCredentials() {
        return null;
    }

    @Override
    public Object getPrincipal() {
        return this.email; // 이메일을 주 식별자로 사용
    }


}

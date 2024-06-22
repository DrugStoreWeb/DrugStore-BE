package com.github.drug_store_be.config.security;

import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class SocialAuthenticationProvider implements AuthenticationProvider {
    private final UserRepository userRepository;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        SocialAuthenticationToken authToken = (SocialAuthenticationToken) authentication;
        String email = (String) authToken.getPrincipal();
        String kakaoUserId = authToken.getKakaoUserId();
        User user = userRepository.findByEmailFetchJoin(email)
                .orElseThrow(() -> new UsernameNotFoundException("이메일에 해당하는 유저를 찾을 수 없습니다."));


        return new SocialAuthenticationToken(email,kakaoUserId);
    }

    @Override
    public boolean supports(Class<?> authentication) {
        return SocialAuthenticationToken.class.isAssignableFrom(authentication);
    }
}

package com.github.drug_store_be.service.security;

import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userDetails.PrincipalDetails;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.security.oauth2.client.userinfo.DefaultOAuth2UserService;
import org.springframework.security.oauth2.client.userinfo.OAuth2UserRequest;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.user.OAuth2User;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomOAuth2UserService extends DefaultOAuth2UserService {

    private final UserJpa userJpa;

    //OAuth2UserRequest는 인증 서버의 클라이언트 ID, 클라이언트 시크릿, 그리고 액세스 토큰 등의 정보를 포함
    @Override
    public OAuth2User loadUser(OAuth2UserRequest request) throws OAuth2AuthenticationException{
        OAuth2User oauth2User=super.loadUser(request);
        //OAuth2UserService 인터페이스의 loadUser 메서드는 OAuth2UserRequest를 인수로 받아, 액세스 토큰을 사용하여 사용자 정보를 로드
        //super.loadUser(userRequest)는 DefaultOAuth2UserService 등의 기본 구현을 호출하여 사용자 정보를 가져옵니다.
        // 이 기본 구현은 주어진 액세스 토큰을 사용하여 사용자 정보 엔드포인트에 HTTP 요청을 보내고, 사용자 정보를 포함하는 응답을 받습니다.
        //즉 oAuth2User은 사용자 정보가 담긴 객체

        String kakaoUserNum = oauth2User.getAttributes().get("id").toString();
        String email = ((Map<String, Object>) oauth2User.getAttributes().get("kakao_account")).get("email").toString();

        Optional<User> userOpt = userJpa.findByKakaoNum(kakaoUserNum);

        if (userOpt.isPresent()) {
            //1.카카오 회원번호가 있음(카카오 연동됨)
            User user = userOpt.get();
            return new PrincipalDetails(user, oauth2User.getAttributes());
        } else {
            userOpt = userJpa.findByEmailFetchJoin(email);
            if (userOpt.isPresent()) {
                //2. 이메일로 회원가입은 되어있으나 카카오 연동은 안됨.
                User user = userOpt.get();
                user.setKakaoNum(kakaoUserNum);
                userJpa.save(user);
                return new PrincipalDetails(user, oauth2User.getAttributes());
            } else {
                // 3. 회원가입이 안되어 있거나 등록된 이메일과 다름.
                throw new NotFoundException("이메일을 찾을 수 없습니다. 회원가입 후 이용 바랍니다.");
            }
        }
    }
}

//전체 흐름 정리
//OAuth2 로그인 요청: 사용자가 OAuth2 공급자를 통해 로그인 시도.
//OAuth2UserRequest 생성: Spring Security가 내부적으로 OAuth2UserRequest 객체를 생성.
//loadUser 호출: Spring Security가 OAuth2UserRequest를 CustomOAuth2UserService의 loadUser 메서드에 전달.
//사용자 정보 로드 및 처리: CustomOAuth2UserService에서 사용자 정보를 로드하고 추가 처리.
//로그인 성공 처리: CustomAuthenticationSuccessHandler에서 로그인 성공 후 추가 로직 처리.
//이 과정을 통해 OAuth2UserRequest 객체는 Spring Security에 의해 자동으로 생성되고 DefaultOAuth2UserService에 전달되며, 이를 통해 loadUser 메서드가 호출되어 사용자 정보를 로드하게 됩니다.

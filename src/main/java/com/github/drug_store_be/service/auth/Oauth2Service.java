//package com.github.drug_store_be.service.auth;
//
//import com.fasterxml.jackson.core.JsonProcessingException;
//import com.fasterxml.jackson.databind.JsonNode;
//import com.fasterxml.jackson.databind.ObjectMapper;
//import com.github.drug_store_be.config.properties.KakaoProperties;
//import com.github.drug_store_be.config.security.JwtTokenProvider;
//import com.github.drug_store_be.config.security.SocialAuthenticationToken;
//import com.github.drug_store_be.repository.role.Role;
//import com.github.drug_store_be.repository.role.RoleRepository;
//import com.github.drug_store_be.repository.user.User;
//import com.github.drug_store_be.repository.user.UserRepository;
//import com.github.drug_store_be.repository.userRole.UserRole;
//import com.github.drug_store_be.repository.userRole.UserRoleRepository;
//import com.github.drug_store_be.service.exceptions.NotFoundException;
//import jakarta.servlet.http.HttpServletResponse;
//import lombok.RequiredArgsConstructor;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.http.*;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.context.SecurityContextHolder;
//import org.springframework.security.crypto.password.PasswordEncoder;
//import org.springframework.stereotype.Service;
//import org.springframework.transaction.annotation.Transactional;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.stream.Collectors;
//
//@Service
//@RequiredArgsConstructor
//@Slf4j
//public class Oauth2Service {
//    private final UserRepository userRepository;
//    private final RoleRepository roleRepository;
//    private final UserRoleRepository userRoleRepository;
//    private final PasswordEncoder passwordEncoder;
//    private final JwtTokenProvider jwtTokenProvider;
//    private final KakaoProperties kakaoProperties;
//
//
//
//    public void redirectKakaoAuth(HttpServletResponse response) {
//        String kakaoUrl = kakaoProperties.getAuthorizationUri()
//                + "?client_id=" + kakaoProperties.getClientId()
//                + "&redirect_uri=" + kakaoProperties.getRedirectUri()
//                + "&response_type=code";
//        try {
//            response.sendRedirect(kakaoUrl);
//        } catch (IOException ioException) {
//            throw new NotFoundException("해당 주소를 찾을 수 없습니다.");
//        }
//    }
//    @Transactional
//    public String callBack(String code) {
//        String accessToken = getKakaoAccessToken(code);
//        String userInfo = getKakaoUserInfo(accessToken);
//        ObjectMapper objectMapper = new ObjectMapper();
//        try {
//            JsonNode rootNode = objectMapper.readTree(userInfo);
//            String kakaoUserId = rootNode.path("id").asText();
//            String name = rootNode.path("properties").path("nickname").asText();
//            String profileImg = rootNode.path("properties").path("profile_image").asText();
//            String email = rootNode.path("kakao_account").path("email").asText();
//
//            User user = null;
//
//            if (!userRepository.existsByEmail(email)) {
//                user=saveUser(profileImg,name,email,kakaoUserId);
//
//            }
//
//            user= userRepository.findByEmailFetchJoin(email)
//                    .orElseThrow(() -> new NotFoundException("이메일 또는 비밀번호를 잘못 입력했습니다.\n" +
//                            "입력하신 내용을 다시 확인해주세요."));
//
//            log.info("결과:"+user.getUserRole());
//
//            Authentication authentication = new SocialAuthenticationToken(email,kakaoUserId);
//            SecurityContextHolder.getContext().setAuthentication(authentication);
//
//            List<UserRole> rolename =userRoleRepository.findAllByUser(user);
//            List<String> roleNameList= rolename.stream().map(UserRole::getRole).map(Role::getRoleName).toList();
//            log.info("역할 이름:"+roleNameList);
//
//
//            return jwtTokenProvider.createToken(email, roleNameList);
//
//        } catch (JsonProcessingException e ) {
//
//            // 예외 처리
//            return "토큰 얻기 실패";
//        }catch(NotFoundException nfe){
//            return "토큰 얻기 실패";
//        }
//
//    }
//    //메소드
//    //유저정보 추출
//    private String getKakaoUserInfo(String accessToken) {
//        RestTemplate restTemplate = new RestTemplate();
//
//        String url = kakaoProperties.getUserInfoUri();
//
//        HttpHeaders headers = new HttpHeaders();
//        headers.add("Authorization", "Bearer " + accessToken);
//
//        HttpEntity<String> request = new HttpEntity<>(headers);
//
//        log.info("카카오에게 사용자 정보를 가져오기 위해 요청을 보내는 중입니다.");
//
//        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, request, String.class);
//
//        if (response.getStatusCode() == HttpStatus.OK) {
//            log.info("사용자 정보 얻기 성공");
//            return response.getBody();
//        }
//
//        log.info("유저 정보 얻기 실패: " + response.getBody());
//        throw new NotFoundException("유저 정보를 얻는데 실패하였습니다.");
//
//    }
//
//    //메소드
//    //code로 토큰 추출
//    private String getKakaoAccessToken(String code) {
//        RestTemplate restTemplate = new RestTemplate();
//        HttpHeaders headers = new HttpHeaders();
//        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
//
//        MultiValueMap<String, String> params = new LinkedMultiValueMap<>();
//        params.add("grant_type", "authorization_code");
//        params.add("client_id", kakaoProperties.getClientId());
//        params.add("redirect_uri", kakaoProperties.getRedirectUri());
//        params.add("code", code);
//        params.add("client_secret", kakaoProperties.getClientSecret());
//        HttpEntity<MultiValueMap<String, String>> request = new HttpEntity<>(params, headers);
//        // 요청을 보내는 부분 수정
//        ResponseEntity<Map> response = restTemplate.postForEntity(kakaoProperties.getTokenUri(), request, Map.class);
//        //수정된 부분
//        if (response.getStatusCode() == HttpStatus.OK) {
//            // 요청이 성공했을 때
//            log.error("access token: " + response.getBody().get("access_token"));
//            return response.getBody().get("access_token").toString();
//        } else {
//            // 요청이 실패했을 때
//            log.error(" access token 얻기 실패: " + response.getBody());
//            // 예외 처리 등 필요한 작업 수행
//            throw new RuntimeException("access token 얻기 실패하였습니다.");
//        }
//    }
//    public User saveUser(String profileImg, String name , String email , String password){
//        // UserEntity에 저장
//        User user = User.of(profileImg,name,email);
//        user.setPassword(passwordEncoder.encode(password));
//
//        // UserEntity를 DB에 저장하는 코드 작성
//        userRepository.save(user);
//        Role role = roleRepository.findByRoleName("ROLE_USER")
//                .orElseThrow(() -> new NotFoundException("code : " + HttpStatus.NOT_FOUND.value() + " USER라는 역할이 없습니다."));
//        UserRole signUpUserRole = UserRole.builder()
//                .role(role)
//                .user(user)
//                .build();
//        userRoleRepository.save(signUpUserRole);
//        return user;
//    }
//}

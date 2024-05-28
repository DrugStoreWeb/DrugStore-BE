package com.github.drug_store_be.service.auth;

import com.github.drug_store_be.config.security.JwtTokenProvider;
import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.role.RoleJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.repository.userRole.UserRoleJpa;
import com.github.drug_store_be.service.exceptions.NotAcceptException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Auth.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserJpa userJpa;
    private final UserRoleJpa userRoleJpa;
    private final RoleJpa roleJpa;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;


    public ResponseDto signUpResult(SignUp signUpRequest) {
        if (userJpa.existsByEmail(signUpRequest.getEmail())){
            CheckResponse checkResponse = new CheckResponse(signUpRequest.getEmail()+"(는)은 이미 존재하는 이메일입니다. 다른 이메일을 이용헤주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordCheck())){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호 체크란이 비밀번호와 동일하지 않습니다.");
        }

        if (userJpa.existsByNickname(signUpRequest.getNickname())){
            CheckResponse checkResponse = new CheckResponse(signUpRequest.getNickname() + "(는)은 이미 존재하는 닉네임입니다. 다른 닉네임을 이용해주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }

        Role role =roleJpa.findByRoleName("ROLE_USER")
                .orElseThrow(()-> new NotFoundException("code : "+HttpStatus.NOT_FOUND.value()+" USER라는 역할이 없습니다."));
        User signUpUser = User.builder()
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .birthday(signUpRequest.getBirthday())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .address(signUpRequest.getAddress())
                .profilePic(signUpRequest.getProfilePic())
                .money(0)
                .build();
        userJpa.save(signUpUser);
        UserRole signUpUserRole = UserRole.builder()
                .role(role).user(signUpUser)
                .build();
        userRoleJpa.save(signUpUserRole);
        return new ResponseDto(HttpStatus.OK.value(),signUpRequest.getName()+ "님 회원 가입에 성공하셨습니다.");
    }

    public String login(Login loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user =userJpa.findByEmailFetchJoin(email)
                    .orElseThrow(() -> new NotFoundException("이메일 또는 비밀번호를 잘못 입력했습니다.\n" +
                            "입력하신 내용을 다시 확인해주세요."));
            List<String> roles = user.getUserRole()
                    .stream().map(UserRole::getRole)
                    .map(Role::getRoleName)
                    .collect(Collectors.toList());

            return jwtTokenProvider.createToken(email, roles);
        }catch (NotFoundException e){
            throw e;
        }catch(Exception e){
            e.printStackTrace();
            throw new NotAcceptException("이메일 또는 비밀번호를 잘못 입력했습니다. 입력하신 내용을 다시 확인해주세요.");
        }
    }

    public ResponseDto nicknameCheckResult(NicknameCheck nicknameCheck) {
        if (userJpa.existsByNickname(nicknameCheck.getNickname())){

            CheckResponse checkResponse = new CheckResponse(nicknameCheck.getNickname() + "(는)은 이미 존재하는 닉네임입니다. 다른 닉네임을 이용해주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }else {
            CheckResponse checkResponse = new CheckResponse(nicknameCheck.getNickname() + "(는)은 사용하실 수 있는 닉네임입니다.",false);
            return new ResponseDto(HttpStatus.OK.value(),"중복 여부 확인",checkResponse );
        }
    }

    public ResponseDto emailCheckResult(EmailCheck emailCheck) {
        String email=emailCheck.getEmail().toLowerCase();
        if (userJpa.existsByEmail(email)){
            CheckResponse checkResponse = new CheckResponse(emailCheck.getEmail()+"(는)은 이미 존재하는 이메일입니다. 다른 이메일을 이용헤주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }else {
            CheckResponse checkResponse = new CheckResponse(emailCheck.getEmail()+"(는)은 사용하실 수 있는 이메일입니다.",false);
            return new ResponseDto(HttpStatus.OK.value(), "중복 여부 확인",checkResponse);
        }

    }
    public ResponseDto findEmailResult(FindEmail findEmail) {
        User user = userJpa.findByNicknameAndPhoneNumber(findEmail.getNickname(),findEmail.getPhoneNum())
                .orElseThrow(()->new NotFoundException("닉네임과 휴대폰에 해당하는 유저를 찾을 수 없습니다."));
        String userEmail = user.getEmail();
        return new ResponseDto(HttpStatus.OK.value(),"email : " + userEmail);
    }
@Transactional
    public ResponseDto changePasswordResult(ChangePassword changePassword) {
        User user =userJpa.findByEmailFetchJoin(changePassword.getEmail())
                .orElseThrow(()-> new NotFoundException("가입되지 않은 이메일입니다."));
        if (!changePassword.getNewPassword().equals(changePassword.getNewPasswordCheck())){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호 체크란이 비밀번호와 동일하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        return new ResponseDto(HttpStatus.OK.value(), "변경된 비밀번호로 다시 로그인해주세요");
    }
}

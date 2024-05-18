package com.github.drug_store_be.service.auth;

import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.role.RoleJpa;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserJpa;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.repository.userRole.UserRoleJpa;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Auth.SignUp;
import com.github.drug_store_be.web.DTO.ResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final UserJpa userJpa;
    private final UserRoleJpa userRoleJpa;
    private final RoleJpa roleJpa;
    private final PasswordEncoder passwordEncoder;
    public ResponseDto signUpResult(SignUp signUpRequest) {
        if (userJpa.existsByEmail(signUpRequest.getEmail())){
            return new ResponseDto(HttpStatus.CONFLICT.value(), signUpRequest.getEmail()+"은 이미 존재하는 이메일입니다. 다른 이메일을 이용헤주세요.");
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordCheck())){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호 체크란이 비밀번호와 동일하지 않습니다.");
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
}

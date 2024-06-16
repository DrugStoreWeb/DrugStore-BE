package com.github.drug_store_be.service.auth;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;
import com.amazonaws.services.s3.model.PutObjectRequest;
import com.github.drug_store_be.config.security.JwtTokenProvider;
import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.role.RoleRepository;
import com.github.drug_store_be.repository.user.User;
import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.repository.userRole.UserRoleRepository;
import com.github.drug_store_be.service.exceptions.NotAcceptException;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.service.exceptions.StorageUpdateFailedException;
import com.github.drug_store_be.web.DTO.Auth.*;
import com.github.drug_store_be.web.DTO.ResponseDto;
import com.github.drug_store_be.web.DTO.awsS3.SaveFileType;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Objects;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final JwtTokenProvider jwtTokenProvider;
    private final UserRepository userRepository;
    private final UserRoleRepository userRoleRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final AmazonS3 amazonS3Client;
    @Value("${cloud.aws.s3.bucket-name}")
    private String bucketName;

    @Transactional
    public ResponseDto signUpResult(SignUp signUpRequest, MultipartFile multipartFiles) {
        if (userRepository.existsByEmail(signUpRequest.getEmail())){
            CheckResponse checkResponse = new CheckResponse(signUpRequest.getEmail()+"(는)은 이미 존재하는 이메일입니다. 다른 이메일을 이용헤주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }
        if (!signUpRequest.getPassword().equals(signUpRequest.getPasswordCheck())){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호 체크란이 비밀번호와 동일하지 않습니다.");
        }

        if (userRepository.existsByNickname(signUpRequest.getNickname())){
            CheckResponse checkResponse = new CheckResponse(signUpRequest.getNickname() + "(는)은 이미 존재하는 닉네임입니다. 다른 닉네임을 이용해주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }
        SaveFileType type =SaveFileType.small;

        Role role =roleRepository.findByRoleName("ROLE_USER")
                .orElseThrow(()-> new NotFoundException("code : "+HttpStatus.NOT_FOUND.value()+" USER라는 역할이 없습니다."));
        User signUpUser = User.createUser(signUpRequest,passwordEncoder);
        userProfileSave(type,multipartFiles,signUpUser);
        userRepository.save(signUpUser);
        UserRole signUpUserRole = UserRole.signUpUserRole(role,signUpUser);
        userRoleRepository.save(signUpUserRole);
        return new ResponseDto(HttpStatus.OK.value(),signUpRequest.getName()+ "님 회원 가입에 성공하셨습니다.");
    }
    //프로필 이미지 저장 메소드
    public void userProfileSave(SaveFileType type,MultipartFile multipartFiles,User signUpUser){
        switch(type){
            case small:
                PutObjectRequest putObjectRequest= makePutObjectRequest(multipartFiles);
                amazonS3Client.putObject(putObjectRequest);
                String url= amazonS3Client.getUrl(bucketName, putObjectRequest.getKey()).toString();
                signUpUser.setProfilePic(url);
                break;
            case large:
                break;
        }

    }
    //트러블 슈팅 정리하기 90번의 exception 없을 경우 예외처리가 정상 발동하지 않는다.
    //구글, 챗봇에서 확인 한 결과 notFoundException 예외처리가 catch에서 정의되어 해당 오류를 잡아야하는데
    //해당 오류는 없고 바로 최종보스 exception만 남아서 정상 처리 못함
    public String login(Login loginRequest) {
        String email = loginRequest.getEmail();
        String password = loginRequest.getPassword();

        try{
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(email,password));
            SecurityContextHolder.getContext().setAuthentication(authentication);

            User user =userRepository.findByEmailFetchJoin(email)
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
        if (userRepository.existsByNickname(nicknameCheck.getNickname())){

            CheckResponse checkResponse = new CheckResponse(nicknameCheck.getNickname() + "(는)은 이미 존재하는 닉네임입니다. 다른 닉네임을 이용해주세요.",true);
            return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
        }else {
            CheckResponse checkResponse = new CheckResponse(nicknameCheck.getNickname() + "(는)은 사용하실 수 있는 닉네임입니다.",false);
            return new ResponseDto(HttpStatus.OK.value(),"중복 여부 확인",checkResponse );
        }
    }

    public ResponseDto emailCheckResult(EmailCheck emailCheck) {
        String email=emailCheck.getEmail().toLowerCase();
            if (userRepository.existsByEmail(email)){
                CheckResponse checkResponse = new CheckResponse(emailCheck.getEmail()+"(는)은 이미 존재하는 이메일입니다. 다른 이메일을 이용헤주세요.",true);
                return new ResponseDto(HttpStatus.CONFLICT.value(), "중복 여부 확인",checkResponse);
            }else {
                CheckResponse checkResponse = new CheckResponse(emailCheck.getEmail()+"(는)은 사용하실 수 있는 이메일입니다.",false);
                return new ResponseDto(HttpStatus.OK.value(), "중복 여부 확인",checkResponse);
            }

    }
    public ResponseDto findEmailResult(FindEmail findEmail) {
        User user = userRepository.findByNicknameAndPhoneNumber(findEmail.getNickname(),findEmail.getPhoneNum())
                .orElseThrow(()->new NotFoundException("닉네임과 휴대폰에 해당하는 유저를 찾을 수 없습니다."));
        String userEmail = user.getEmail();
        return new ResponseDto(HttpStatus.OK.value(),"email : " + userEmail);
    }
@Transactional
    public ResponseDto changePasswordResult(ChangePassword changePassword) {
        User user =userRepository.findByEmailFetchJoin(changePassword.getEmail())
                .orElseThrow(()-> new NotFoundException("가입되지 않은 이메일입니다."));
        if (!changePassword.getNewPassword().equals(changePassword.getNewPasswordCheck())){
            return new ResponseDto(HttpStatus.BAD_REQUEST.value(), "비밀번호 체크란이 비밀번호와 동일하지 않습니다.");
        }
        user.setPassword(passwordEncoder.encode(changePassword.getNewPassword()));
        return new ResponseDto(HttpStatus.OK.value(), "변경된 비밀번호로 다시 로그인해주세요");
    }


    //메소드
    private PutObjectRequest makePutObjectRequest(MultipartFile file) {
        String storageFileName= makeStorageFileName(Objects.requireNonNull(file.getOriginalFilename()));
        ObjectMetadata objectMetadata= new ObjectMetadata();
        objectMetadata.setContentType(file.getContentType());
        objectMetadata.setContentLength(file.getSize());
        try{
            return new PutObjectRequest(bucketName, storageFileName, file.getInputStream(), objectMetadata);
        } catch (IOException e) {
            throw new StorageUpdateFailedException("파일 업로드 실패", file.getOriginalFilename());
        }
    }

    private String makeStorageFileName(String orignialFileName) {
        String extension= orignialFileName.substring(orignialFileName.lastIndexOf(".")+1);
        return UUID.randomUUID() + "." + extension;
    }
}

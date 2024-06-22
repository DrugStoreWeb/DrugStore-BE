package com.github.drug_store_be.repository.user;

import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.userCoupon.UserCoupon;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import com.github.drug_store_be.web.DTO.Auth.SignUp;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of="userId")
@Builder
@Entity
@Table(name="user")
public class User{
    //test
    @Id@GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Integer userId;
    @Column(name="name",length=45,nullable = false)
    private String name;
    @Column(name="nickname",length=45)
    private String nickname;
    @Column(name="email",length=100,nullable = false)
    private String email;
    @Column(name="password",length=100,nullable = false)
    private String password;
    @Column(name="birthday",nullable = false)
    private LocalDate birthday;
    @Column(name="phone_number",length=11,nullable = false)
    private String phoneNumber;
    @Column(name="address",length=255,nullable = false)
    private String address;
    @Column(name="profile_pic",length=255,nullable = false)
    private String profilePic;
    @Column(name="money",nullable = false)
    private Integer money;
    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserRole> userRole;
    @OneToMany(mappedBy= "user", cascade = CascadeType.ALL,orphanRemoval = true)
    private List<UserCoupon> userCouponList;

    public User(String profileImg,String name,String email){
        this.profilePic=profileImg;
        this.name="kakao_"+name;
        this.email=email;
        this.phoneNumber="카카오 로그인";
        this.address="카카오 로그인";
        this.nickname="kakao_"+name;
        this.money=0;
        this.birthday=LocalDate.now();
    }
    public static User of(String profileImg,String name,String email){
        return new User(profileImg,name,email);
    }

    //kakaoPay
//    @Column(name="tid",nullable = false)
//    private String tid;
//
//    public void updateTid(String tid) {
//        this.tid = tid;
//    }
    public String getRoleName(){
        return  getUserRole().stream()
                .map(UserRole::getRole)
                .map(Role::getRoleName)
                .findFirst().orElseThrow(()->new NotFoundException("유저에게 역할이 없습니다."));
    }
    public static User findUser(CustomUserDetails customUserDetails,UserRepository userRepository){
        String userEmail = customUserDetails.getEmail();
        return userRepository.findByEmailFetchJoin(userEmail)
                .orElseThrow(()-> new NotFoundException("유저를 찾을 수 없습니다."));
    }
    public static User createUser(SignUp signUpRequest, PasswordEncoder passwordEncoder){
       return User.builder()
                .name(signUpRequest.getName())
                .nickname(signUpRequest.getNickname())
                .email(signUpRequest.getEmail())
                .password(passwordEncoder.encode(signUpRequest.getPassword()))
                .birthday(signUpRequest.getBirthday())
                .phoneNumber(signUpRequest.getPhoneNumber())
                .address(signUpRequest.getAddress())
                .money(0)
                .build();
    }
}
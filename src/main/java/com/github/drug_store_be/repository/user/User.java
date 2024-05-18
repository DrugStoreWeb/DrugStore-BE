package com.github.drug_store_be.repository.user;

import com.github.drug_store_be.repository.userRole.UserRole;
import jakarta.persistence.*;
import lombok.*;

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
}
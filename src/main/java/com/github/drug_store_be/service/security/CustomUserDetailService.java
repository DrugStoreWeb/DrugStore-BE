package com.github.drug_store_be.service.security;

import com.github.drug_store_be.repository.role.Role;
import com.github.drug_store_be.repository.user.User;

import com.github.drug_store_be.repository.user.UserRepository;
import com.github.drug_store_be.repository.userDetails.CustomUserDetails;
import com.github.drug_store_be.repository.userRole.UserRole;
import com.github.drug_store_be.service.exceptions.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.mapstruct.control.MappingControl;
import org.springframework.context.annotation.Primary;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.stream.Collectors;

@Service
@Primary
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {

    private final UserRepository userRepository;
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user =userRepository.findByEmailFetchJoin(email)
                .orElseThrow(()-> new NotFoundException("email:"+email+"에 해당하는 user가 없습니다."));
       return CustomUserDetails.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getUserRole().stream().map(UserRole::getRole).map(Role::getRoleName).collect(Collectors.toList()))
                .build();

    }
}

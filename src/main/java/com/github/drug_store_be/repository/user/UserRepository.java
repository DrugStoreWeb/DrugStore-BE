package com.github.drug_store_be.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.userRole ur JOIN FETCH ur.role WHERE u.email = :email ")
    Optional<User> findByEmailFetchJoin(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

    @Query("SELECT u.userId FROM User u WHERE u.email = ?1")
    Optional<Integer> findByEmail(String email);

    Optional<User> findByUserId(Integer userId);
    Optional<User> findByNicknameAndPhoneNumber(String nickname,String phoneNum);
}

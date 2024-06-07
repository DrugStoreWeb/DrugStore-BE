package com.github.drug_store_be.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserJpa extends JpaRepository<User,Integer> {
    @Query("SELECT u FROM User u JOIN FETCH u.userRole ur JOIN FETCH ur.role WHERE u.email = :email ")
    Optional<User> findByEmailFetchJoin(String email);
    boolean existsByEmail(String email);
    boolean existsByNickname(String nickname);

//    @Query("SELECT u FROM User u WHERE u.email = :email")
//    Optional<User> findByEmail(String email);


//    @Query("SELECT u FROM User u " +
//            "WHERE u.email = :name ")

}

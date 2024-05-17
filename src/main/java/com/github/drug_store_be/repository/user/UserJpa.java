package com.github.drug_store_be.repository.user;


import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserJpa extends JpaRepository<User,Integer> {
}

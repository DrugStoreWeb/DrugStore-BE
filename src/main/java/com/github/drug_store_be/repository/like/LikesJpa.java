package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {
    Optional<Object> findByUserAndLike(User user, Likes like);
}

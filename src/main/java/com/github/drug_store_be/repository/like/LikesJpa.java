package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;


@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {
    List<Likes> findByUser(User user);
    boolean existsByUserAndProduct(User user, Product product);
    Optional<Likes> findByUserAndProduct(User user, Product product);
}

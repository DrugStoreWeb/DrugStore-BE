package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {
    Boolean existsByUserAndProduct(User user,Product product);
    Likes findByUserAndProduct(User user,Product product);
}

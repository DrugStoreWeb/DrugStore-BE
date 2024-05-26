package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {

//    @Query("SELECT l.product FROM Likes l GROUP BY l.product ORDER BY COUNT(l.likesId) DESC")
//    static Product findTopByOrderByLikesDesc();

    @Query("SELECT l FROM Likes l WHERE l.user.userId = :userId AND l.product.productId = :productId")
    Likes findByUserIdAndProductId(int userId, Integer productId);

    @Query("SELECT SUM(l.likesId) FROM Likes l WHERE l.product.productId = :productId")
    int findByProductId(int productId);


}

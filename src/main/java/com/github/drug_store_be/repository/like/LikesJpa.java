package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {

//    @Query("SELECT l.product FROM Likes l GROUP BY l.product ORDER BY COUNT(l.likesId) DESC")
//    static Product findTopByOrderByLikesDesc();

    @Query("SELECT l FROM Likes l WHERE l.user.userId = :userId AND l.product.productId = :productId")
    Likes findByUserIdAndProductId(Optional<Integer> userId, Integer productId);

    @Query("SELECT COUNT(l.likesId) FROM Likes l WHERE l.product.productId = :productId")
    Integer findByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT l.product FROM Likes l GROUP BY l.product ORDER BY COUNT(l) DESC, l.product.productId ASC limit 1")
    Product findProductWithMostLikes();

}

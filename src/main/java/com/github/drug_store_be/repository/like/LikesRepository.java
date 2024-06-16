package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;


import java.util.Optional;

@Repository
public interface LikesRepository extends JpaRepository<Likes,Integer> {
    List<Likes> findByUser(User user);
    void deleteByUserAndProduct(User user, Product product);
    Boolean existsByUserAndProduct(User user,Product product);
    Likes findByUserAndProduct(User user,Product product);

//    @Query("SELECT l.product FROM Likes l GROUP BY l.product ORDER BY COUNT(l.likesId) DESC")
//    static Product findTopByOrderByLikesDesc();

    @Query("SELECT l FROM Likes l WHERE l.user.userId = :userId AND l.product.productId = :productId")
    Likes findByUserIdAndProductId(Optional<Integer> userId, Integer productId);

    @Query("SELECT COUNT(l.likesId) FROM Likes l WHERE l.product.productId = :productId")
    Integer findByProductId(@Param("productId") Integer productId);

    @Query(value = "SELECT l.product FROM Likes l GROUP BY l.product ORDER BY COUNT(l) DESC, l.product.productId ASC limit 1")
    Product findProductWithMostLikes();

}

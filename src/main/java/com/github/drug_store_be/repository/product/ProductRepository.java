package com.github.drug_store_be.repository.product;


import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;

@Repository
public interface ProductRepository extends JpaRepository<Product,Integer> {
    Optional<Product> findById(Integer productId);
    List<Product> findByCategoryCategoryId(int category);

    @Transactional
    @Modifying
    @Query(value = "UPDATE product p SET p.product_sales = " +
            "CASE WHEN p.original_stock = 0 THEN 0 " +
            "ELSE ((p.original_stock - COALESCE((SELECT SUM(o.stock) FROM options o WHERE o.product_id = p.product_id), 0)) * 100) / p.original_stock " +
            "END", nativeQuery = true)
    void updateProductSales();

    @Transactional
    @Modifying
    @Query("UPDATE Product p " +
            "SET p.reviewAvg = (SELECT ROUND(AVG(r.reviewScore),1) FROM Review r WHERE r.product = p) " +
            "WHERE p IN (SELECT r.product FROM Review r)")
    void updateReviewAvg();


    Product findTopByOrderByProductSalesDesc();
    Product findTopByOrderByReviewAvgDesc();
    @Query("SELECT p FROM Product p WHERE p.productId = (SELECT l.product.productId FROM Likes l GROUP BY l.product.productId ORDER BY COUNT(l.product.productId) DESC limit 1)")
    Product findTopByOrderByLikesDesc();




    //두 글자 이상 일치하는 검색어 찾기
    @Query("SELECT p FROM Product p WHERE LENGTH(:keyword) >= 2 AND (p.brand LIKE %:keyword% OR p.productName LIKE %:keyword%)")
    List<Product> findByKeyword(@Param("keyword") String keyword);
    @Query("SELECT COUNT(l) FROM Likes l JOIN l.product p WHERE p.productId = :productId")
    int countLikesByProductId(@Param("productId") Integer productId);
    @Query("SELECT CASE WHEN COUNT(l) > 0 THEN true ELSE false END " +
            "FROM Likes l WHERE l.user.userId = :userId AND l.product.productId = :productId")
    Boolean existsByUserIdAndProductId(@Param("productId") Integer productId,@Param("userId") Integer userId);
}

package com.github.drug_store_be.repository.product;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpa extends JpaRepository<Product,Integer> {
    @Query("SELECT p FROM Product p WHERE p.productId=:productId" )
    Optional<Product> findById(Integer productId);

    @Transactional
    @Modifying
    @Query("UPDATE Product p SET p.productSales = CASE " +
            "WHEN p.originalStock = 0 THEN 0 " +
            "ELSE (p.originalStock - (SELECT COALESCE(SUM(o.stock), 0) FROM Options o WHERE o.product.productId = p.productId)) / p.originalStock * 100 " +
            "END")
    void updateProductSales();

    @Transactional
    @Modifying
    @Query("UPDATE Product p " +
            "SET p.reviewAvg = (SELECT ROUND(AVG(r.reviewScore),1) FROM Review r WHERE r.product = p) " +
            "WHERE p IN (SELECT r.product FROM Review r)")
    void updateReviewAvg();

    @Query("SELECT p FROM Product p ORDER BY p.productSales DESC limit 1")
    Product findTopByOrderByProductSalesDesc();

    @Query("SELECT p FROM Product p ORDER BY p.reviewAvg DESC limit 1")
    Product findTopByOrderByReviewAvgDesc();
    @Query("SELECT p FROM Product p WHERE p.category.categoryId=:category")
    List<Product> findByCategory(int category);

    //세 글자 이상 일치하는 검색어 찾기
    @Query("SELECT p FROM Product p WHERE LENGTH(:keyword) >= 3 AND (p.brand LIKE %:keyword% OR p.productName LIKE %:keyword%)")
    List<Product> findByBrandOrProductNameContaining(@Param("keyword") String keyword);
}

package com.github.drug_store_be.repository.product;

import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductJpa extends JpaRepository<Product,Integer> {
    @Modifying
    @Query(value = "UPDATE Product p " +
            "SET p.productSales = p.originalStock - :totalOptionsStock " +
            "WHERE p.productId = :productId")
    void updateProductSales(@Param("productId") Integer productId,
                            @Param("totalOptionsStock") Integer totalOptionsStock);

    @Query("UPDATE Product p " +
            "SET p.reviewAvg = (SELECT AVG(r.reviewScore) FROM Review r WHERE r.product = p) " +
            "WHERE p IN (SELECT r.product FROM Review r)")
    void updateReviewAvg();

    Product findTopByOrderByReviewCountDesc();

    Product findTopByOrderBySalesDesc();

    Product findTopByOrderByLikesDesc();
//
//    List<Product> findByBrandOrProductName(String keyword);
//    List<Product> findByCategory(String category);
}

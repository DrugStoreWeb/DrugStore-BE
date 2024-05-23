package com.github.drug_store_be.repository.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsJpa extends JpaRepository<Options,Integer> {
    @Query("SELECT o.product.productId, SUM(o.stock) FROM Options o GROUP BY o.product.productId")
    List<Object[]> findTotalOptionsStock();

    @Modifying
    @Query("UPDATE Product p SET p.productSales = (:originalStock - :totalOptionsStock) / :originalStock WHERE p.productId = :productId")
    void updateProductSales(@Param("productId") Long productId, @Param("originalStock") Integer originalStock, @Param("totalOptionsStock") Integer totalOptionsStock);


}

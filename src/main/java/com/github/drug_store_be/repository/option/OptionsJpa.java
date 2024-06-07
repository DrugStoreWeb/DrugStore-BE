package com.github.drug_store_be.repository.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsJpa extends JpaRepository<Options,Integer> {
    @Query(value = "SELECT SUM(stock) FROM Options")
    Integer getTotalOptionsStock();
    @Modifying
    @Query("UPDATE Product p SET p.productSales = (:originalStock - :totalOptionsStock) / :originalStock WHERE p.productId = :productId")
    void updateProductSales(@Param("productId") Long productId, @Param("originalStock") Integer originalStock, @Param("totalOptionsStock") Integer totalOptionsStock);


}

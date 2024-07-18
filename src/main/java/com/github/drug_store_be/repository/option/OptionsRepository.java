package com.github.drug_store_be.repository.option;

import com.github.drug_store_be.repository.product.Product;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OptionsRepository extends JpaRepository<Options,Integer> {
    List<Options> findAllByProduct(Product product);
    @Query(value = "SELECT SUM(stock) FROM Options")
    Integer getTotalOptionsStock();
    @Modifying
    @Query("UPDATE Product p SET p.productSales = (:originalStock - :totalOptionsStock) / :originalStock WHERE p.productId = :productId")
    void updateProductSales(@Param("productId") Long productId, @Param("originalStock") Integer originalStock, @Param("totalOptionsStock") Integer totalOptionsStock);

    List<Options> findAllByProductProductId(Integer productId);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query(
            "SELECT o FROM Options o " +
                    "WHERE o.optionsId = :optionId "
    )
    Optional<Options> findByIdWithLock(int optionId);
}

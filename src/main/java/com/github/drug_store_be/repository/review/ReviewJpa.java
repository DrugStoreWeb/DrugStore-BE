package com.github.drug_store_be.repository.review;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ReviewJpa extends JpaRepository<Review, Integer> {
    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.orders.ordersId = :ordersId")
    Optional<Review> findByUserIdAndOrdersId(@Param("userId") Integer userId, @Param("ordersId") Integer ordersId);

    @Query("SELECT r FROM Review r WHERE r.orders.ordersId = :ordersId")
    Page<Review> findAllReviewsByOrdersId(@Param("ordersId") Integer ordersId, Pageable pageable);
}

package com.github.drug_store_be.repository.order;

import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.review.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrdersRepository extends JpaRepository<Orders,Integer> {

    @Query("SELECT r FROM Orders r WHERE r.user.userId = :userId")
    Page<Orders> findAllByUserId(int userId, Pageable pageable);

//    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.orders.ordersId = :ordersId")
//    Optional<Review> existsByUserIdAndOrdersId(Integer userId, Integer ordersId);
}
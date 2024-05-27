package com.github.drug_store_be.repository.order;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface OrdersJpa extends JpaRepository<Orders,Integer> {

    @Query("SELECT r FROM Orders r WHERE r.user.userId = :userId")
    Page<Orders> findAllByUserId(int userId, Pageable pageable);
}
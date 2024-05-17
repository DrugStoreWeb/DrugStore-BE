package com.github.drug_store_be.repository.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderJpa extends JpaRepository<Order,Integer> {
}
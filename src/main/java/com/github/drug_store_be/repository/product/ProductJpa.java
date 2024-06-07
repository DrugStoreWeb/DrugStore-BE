package com.github.drug_store_be.repository.product;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpa extends JpaRepository<Product,Integer> {
}

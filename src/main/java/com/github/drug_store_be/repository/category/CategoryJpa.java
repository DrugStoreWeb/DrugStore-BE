package com.github.drug_store_be.repository.category;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryJpa extends JpaRepository<Category,Integer> {
}

package com.github.drug_store_be.repository.review;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewJpa extends JpaRepository<Review, Integer> {
}

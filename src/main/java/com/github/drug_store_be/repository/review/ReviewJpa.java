package com.github.drug_store_be.repository.review;

import com.github.drug_store_be.repository.product.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ReviewJpa extends JpaRepository<Review, Integer> {

    List<Review> findAllByProduct(Product product);
    Page<Review> findByProductOrderByCreateAtDesc(Product product,Pageable pageable);
    Page<Review> findByProductOrderByReviewScoreDesc(Product product,Pageable pageable);
    Page<Review> findByProductOrderByReviewScoreAsc(Product product,Pageable pageable);


}

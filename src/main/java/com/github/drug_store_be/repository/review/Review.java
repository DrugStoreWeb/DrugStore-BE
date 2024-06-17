package com.github.drug_store_be.repository.review;

import com.github.drug_store_be.repository.order.Orders;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "review")
@Entity
public class Review {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "review_id")
    private Integer reviewId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "orders_id",nullable = false)
    private Orders orders;

    @Column(name = "review_score", nullable = false)
    private Integer reviewScore;

    @Column(name="review_content", nullable = false)
    private String reviewContent;

    @Column(name = "create_at", nullable = false)
    private LocalDate createAt;
}

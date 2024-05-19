package com.github.drug_store_be.repository.like;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@EqualsAndHashCode(of="likesId")
@Entity
@ToString
@Table(name = "likes")
public class Likes {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "likes_id", nullable = false)
    private Integer likesId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}

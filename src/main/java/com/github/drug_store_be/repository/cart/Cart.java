package com.github.drug_store_be.repository.cart;

import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import lombok.*;

@Entity
@Table(name = "cart")
@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "cart_id")
    private Integer cartId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "options_id", nullable = false)
    private Options options;

    @Column(name = "quantity", nullable = false)
    @Min(value = 1, message = "Quantity must be positive")
    private Integer quantity;
}

package com.github.drug_store_be.repository.order;
import com.github.drug_store_be.repository.cart.Cart;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "orders")
@Entity
public class Orders {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "orders_id", nullable = false)
    private Integer ordersId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cart_id",nullable = false)
    private Cart cart;

    @Column(name = "orders_number",nullable = false, length=20)
    private String ordersNumber;


    @Column(name = "orders_at",nullable = false)
    private LocalDate ordersAt;



}
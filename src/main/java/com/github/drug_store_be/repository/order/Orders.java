package com.github.drug_store_be.repository.order;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of ="ordersId")
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
    @JoinColumn(name = "options_id",nullable = false)
    private Options options;

    @Column(name = "orders_number",nullable = false, length=20)
    private String ordersNumber;


    @Column(name = "orders_at",nullable = false)
    private LocalDate ordersAt;



}
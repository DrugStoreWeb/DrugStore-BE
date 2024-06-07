package com.github.drug_store_be.repository.option;

import com.github.drug_store_be.repository.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "options")
@Entity
public class Options {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "options_id", nullable = false)
    private Integer optionsId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "options_name",nullable = false,length = 20)
    private String optionsName;
    @Column(name = "options_price",nullable = false)
    private Integer optionsPrice;

    @Column(name="options_price",nullable = false)
    private Integer optionsPrice;

    @Column(name="stock",nullable = false)
    private Integer stock;
}

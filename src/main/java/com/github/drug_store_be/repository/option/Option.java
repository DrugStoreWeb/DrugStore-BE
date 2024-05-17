package com.github.drug_store_be.repository.option;

import com.github.drug_store_be.repository.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "option")
@Entity
public class Option {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "option_id", nullable = false)
    private Integer optionId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "option_name",nullable = false,length = 20)
    private String optionName;

    @Column(name="stock",nullable = false)
    private Integer stock;
}
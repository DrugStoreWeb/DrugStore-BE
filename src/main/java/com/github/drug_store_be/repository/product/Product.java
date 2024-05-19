package com.github.drug_store_be.repository.product;
import com.github.drug_store_be.repository.category.Category;
import com.github.drug_store_be.repository.option.Options;
import com.github.drug_store_be.repository.productPhoto.ProductPhoto;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "product")
@Entity
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_id", nullable = false)
    private Integer productId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(name = "product_name", nullable = false, length = 20)
    private String productName;

    @Column(name = "brand", nullable = false, length =10)
    private String brand;

    @Column(name = "price",nullable = false)
    private Integer price;

    @Column(name="product_discount",nullable = false)
    private Integer productDiscount;

    @Column(name="final_price",nullable = false)
    private Integer finalPrice;

    @Column(name = "best",nullable = false)
    private boolean best=false;

    @Column(name = "product_status",nullable = false )
    private boolean productStatus=false;

    @Column(name="create_at",nullable = false)
    private LocalDate createAt;

    @Column(name="original_stock",nullable = false)
    private Integer originalStock;

    @Column(name="product_sales",nullable = false)
    private Double productSales;

    @Column(name="review_avg",nullable = false)
    private Double reviewAvg;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<ProductPhoto> productPhotoList;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Options> optionsList;
}

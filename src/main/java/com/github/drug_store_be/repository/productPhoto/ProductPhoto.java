package com.github.drug_store_be.repository.productPhoto;

import com.github.drug_store_be.repository.product.Product;
import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name= "product_photo")
@Entity
public class ProductPhoto {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "product_photo_id", nullable = false)
    private Integer productPhotoId;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "product_id",nullable = false)
    private Product product;

    @Column(name = "photo_url", nullable = false, length = 255)
    private String photoUrl;

    @Column(name="photo_type",nullable = false)
    private Boolean photoType;
}

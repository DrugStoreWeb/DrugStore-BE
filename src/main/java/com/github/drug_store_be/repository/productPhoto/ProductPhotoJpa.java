package com.github.drug_store_be.repository.productPhoto;

import com.github.drug_store_be.repository.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductPhotoJpa extends JpaRepository<ProductPhoto, Integer> {
}

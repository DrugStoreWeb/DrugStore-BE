package com.github.drug_store_be.repository.productPhoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProductPhotoJpa extends JpaRepository<ProductPhoto,Integer> {
    List<ProductPhoto> findAllByProduct(Product product);
}

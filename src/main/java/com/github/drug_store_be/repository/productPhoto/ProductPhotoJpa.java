package com.github.drug_store_be.repository.productPhoto;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface ProductPhotoJpa extends JpaRepository<ProductPhoto,Integer> {
}

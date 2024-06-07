package com.github.drug_store_be.repository.option;

import com.github.drug_store_be.repository.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OptionsJpa extends JpaRepository<Options,Integer> {
    List<Options> findAllByProduct(Product product);
}

package com.github.drug_store_be.repository.product;

import com.github.drug_store_be.web.DTO.MainPage.productListQueryDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductJpa extends JpaRepository<Product,Integer> {
    Page<productListQueryDto> findAllOrderByLike(PageRequest pageRequest);

    Page<productListQueryDto> findAllOrderByNew(PageRequest pageRequest);

    Page<productListQueryDto> findAllOrderByReview(PageRequest pageRequest);

    Page<productListQueryDto> findAllOrderBySales(PageRequest pageRequest);

    void updateProductSales(Integer productId, Integer originalStock, Integer totalOptionsStock);

    Optional<Object> findById(Long productId);

    }

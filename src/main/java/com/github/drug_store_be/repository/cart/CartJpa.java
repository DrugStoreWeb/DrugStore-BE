package com.github.drug_store_be.repository.cart;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartJpa extends JpaRepository<Cart,Integer> {

    @Query(
            "SELECT c " +
                    "FROM Cart c " +
                    "WHERE c.user.userId =:userId "
    )
    List<Cart> findByUserId(Integer userId);
}

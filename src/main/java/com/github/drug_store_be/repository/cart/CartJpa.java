package com.github.drug_store_be.repository.cart;

import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartJpa extends JpaRepository<Cart,Integer> {
    @Query("SELECT c FROM Cart c WHERE c.user.email = :email")
    List<Cart> findByUserEmail(String email);

    List<Cart> findAllByUser(User user);
}

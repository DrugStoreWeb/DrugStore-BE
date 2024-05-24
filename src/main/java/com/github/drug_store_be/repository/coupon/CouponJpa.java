package com.github.drug_store_be.repository.coupon;

import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CouponJpa extends JpaRepository<Coupon,Integer> {

}

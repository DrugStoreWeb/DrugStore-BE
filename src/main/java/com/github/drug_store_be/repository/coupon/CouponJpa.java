package com.github.drug_store_be.repository.coupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CouponJpa extends JpaRepository<Coupon,Integer> {
}

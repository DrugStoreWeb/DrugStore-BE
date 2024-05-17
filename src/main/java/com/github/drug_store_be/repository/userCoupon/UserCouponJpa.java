package com.github.drug_store_be.repository.userCoupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserCouponJpa extends JpaRepository<UserCoupon,Integer> {
}


package com.github.drug_store_be.repository.userCoupon;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import org.springframework.data.jpa.repository.Query;

@Repository
public interface UserCouponRepository extends JpaRepository<UserCoupon,Integer> {

    @Query("SELECT c FROM UserCoupon c WHERE c.user.userId = :userId")
    List<UserCoupon> findAllByUserId(Integer userId);
}


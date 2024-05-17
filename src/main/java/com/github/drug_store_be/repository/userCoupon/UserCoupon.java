package com.github.drug_store_be.repository.userCoupon;

import com.github.drug_store_be.repository.coupon.Coupon;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "user_coupon")
@Entity
public class UserCoupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_coupon_id", nullable = false)
    private Integer userCouponId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coupon_id",nullable = false)
    private Coupon coupon;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id",nullable = false)
    private User user;
}

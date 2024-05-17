package com.github.drug_store_be.repository.coupon;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name= "coupon")
@Entity
public class Coupon {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "coupon_id", nullable = false)
    private Integer couponId;

    @Column(name = "coupon_name",nullable = false,length = 10)
    private String couponName;

    @Column(name = "coupon_discount",nullable = false)
    private Integer couponDiscount;
}

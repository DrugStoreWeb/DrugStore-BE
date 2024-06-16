package com.github.drug_store_be.repository.review;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.questionAnswer.QuestionAnswer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Integer> {

    List<Review> findAllByProduct(Product product);
    Page<Review> findByProductOrderByCreateAtDesc(Product product,Pageable pageable);
    Page<Review> findByProductOrderByReviewScoreDesc(Product product,Pageable pageable);
    Page<Review> findByProductOrderByReviewScoreAsc(Product product,Pageable pageable);


    @Query("SELECT r FROM Review r WHERE r.user.userId = :userId AND r.orders.ordersId = :ordersId")
    Optional<Review> findByUserIdAndOrdersId(@Param("userId") Integer userId, @Param("ordersId") Integer ordersId);

    @Query("SELECT u FROM Review u WHERE u.user.userId = :userId")
    Page<Review> findAllReviews(Integer userId,Pageable pageable);

//    @Query("SELECT c FROM UserCoupon c WHERE c.user.userId = :userId")
//    List<UserCoupon> findAllByUserId(Integer userId);

    @Query("SELECT q FROM QuestionAnswer q WHERE q.user.userId = :userId")
    List<QuestionAnswer> findAllQnA(Integer userId, Pageable pageable);
}
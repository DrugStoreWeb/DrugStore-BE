package com.github.drug_store_be.repository.questionAnswer;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface QuestionAnswerJpa extends JpaRepository<QuestionAnswer,Integer> {
    List<QuestionAnswer> findByProduct(Product product);
    Optional<QuestionAnswer> findByQuestionAnswerIdAndUser(Integer questionAnswerId, User user);
}

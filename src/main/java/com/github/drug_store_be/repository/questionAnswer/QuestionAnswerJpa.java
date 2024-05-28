package com.github.drug_store_be.repository.questionAnswer;

import com.github.drug_store_be.repository.product.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerJpa extends JpaRepository<QuestionAnswer,Integer> {
    QuestionAnswer findByProductAndQuestion(Product product);
}

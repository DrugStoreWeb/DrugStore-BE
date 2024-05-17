package com.github.drug_store_be.repository.questionAnswer;

import com.github.drug_store_be.repository.product.Product;
import com.github.drug_store_be.repository.user.User;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
@EqualsAndHashCode(of="questionAnswerId")
@Builder
@Entity
@Table(name="question_answer")
public class QuestionAnswer {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="question_answer_id")
    private Integer questionAnswerId;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;
    @Column(name="question",length=300,nullable = false)
    private String question;
    @Column(name="answer",length=300)
    private String answer;
    @Column(name="create_at",nullable = false)
    private LocalDate createAt;
    @Column(name="question_status",nullable = false)
    private Boolean questionStatus;
}

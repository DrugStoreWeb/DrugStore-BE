package com.github.drug_store_be.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LikesJpa extends JpaRepository<Likes,Integer> {

}

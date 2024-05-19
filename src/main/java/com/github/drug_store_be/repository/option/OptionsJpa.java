package com.github.drug_store_be.repository.option;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OptionsJpa extends JpaRepository<Options,Integer> {
}

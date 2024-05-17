package com.github.drug_store_be.repository.role;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleJpa extends JpaRepository<Role,Integer> {
}
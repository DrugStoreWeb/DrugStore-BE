package com.github.drug_store_be;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;

@SpringBootApplication
@EnableCaching
public class DrugStoreBeApplication {

    public static void main(String[] args) {
        SpringApplication.run(DrugStoreBeApplication.class, args);
    }

}

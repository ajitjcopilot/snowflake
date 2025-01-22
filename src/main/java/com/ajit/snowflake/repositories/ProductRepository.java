package com.ajit.snowflake.repositories;

import com.ajit.snowflake.models.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
}

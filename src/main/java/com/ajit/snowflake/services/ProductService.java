package com.ajit.snowflake.services;


import com.ajit.snowflake.configs.SnowflakeIdGenerator;
import com.ajit.snowflake.models.Product;
import com.ajit.snowflake.repositories.ProductRepository;
import org.springframework.stereotype.Service;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final SnowflakeIdGenerator idGenerator;


    public ProductService(ProductRepository productRepository, SnowflakeIdGenerator idGenerator) {
        this.productRepository = productRepository;
        this.idGenerator = idGenerator;
    }


    public Product createProduct(Product product) {
        product.setId(idGenerator.nextId()); // Generate Snowflake ID

        return productRepository.save(product);
    }
}

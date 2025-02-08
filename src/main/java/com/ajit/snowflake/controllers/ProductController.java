package com.ajit.snowflake.controllers;


import com.ajit.snowflake.models.Product;
import com.ajit.snowflake.services.ProductService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/api/products")
@Slf4j
public class ProductController {


    private final ProductService productService;


    public ProductController(ProductService productService) {
        this.productService = productService;
        System.out.println("remove it again");
    }


    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {


       log.info("constructor called B...");
        log.info("constructor called BBBBB...");
        log.info("constructor called snowflake...");
        log.info("constructor called BBBBB11111 f1...");

       log.info("constructor called b1...");
        log.info("constructor called b1b1b1b1...");
        log.info("constructor i m snowflake_1...");
        return new ResponseEntity<>(productService.createProduct(product), HttpStatus.OK);

    }


}



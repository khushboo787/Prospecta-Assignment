package com.prospecta.controller;


import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.prospecta.entity.Product;
import com.prospecta.exception.CategoryNotFoundException;
import com.prospecta.service.ProductService;

import lombok.extern.slf4j.Slf4j;

@RestController
@Slf4j
@RequestMapping("/api/products")
public class ProductController {

    private  ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }
    
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable String category) {
        log.info("Inside the getProductsByCategory method of ProductController Fetching products for category: {}", category);

        
            List<Product> products = productService.getProductsByCategory(category);

            if (products.isEmpty()) {
                throw new CategoryNotFoundException("No products found for category: " + category);
            }

            return new ResponseEntity<>(products, HttpStatus.OK);

         
    }
    @PostMapping("/add")
    public ResponseEntity<Product> addProduct(@RequestBody Product product) {
    	log.info("Inside the addProduct method of ProductController");

            Product addedProduct = productService.addProduct(product);
            return new ResponseEntity<>(addedProduct, HttpStatus.CREATED);
        
    }

     




}
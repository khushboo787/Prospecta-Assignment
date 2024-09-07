package com.prospecta.controller;


import java.util.List;
import java.util.Map;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@RestController
@RequestMapping("/products")
public class ProductController {

    @Value("${fakestore.api.url}")
    private String apiURL;

    private final RestTemplate restTemplate;

    public ProductController(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

     
    @GetMapping("/category/{category}")
    public ResponseEntity<List<Map<String, Object>>> getProductsByCategory(@PathVariable String category) {
      
    	String url = apiURL + "/category/" + category;
       
        ResponseEntity<List> response = restTemplate.getForEntity(url, List.class);
      
        return ResponseEntity.ok(response.getBody());
    
    }
    
    
    
    
    @PostMapping("/add")
    public ResponseEntity<Map<String, Object>> addProduct(@RequestBody Map<String, Object> product) {
     
    	// Log the received product data
    	log.info("Attempting to add a product: {}", product);

    	String url = apiURL;
    	
    	// Attempt to call the FakeStore API to add the product
        log.info("Sending product data to FakeStore API: {}", url);
        
    	
        ResponseEntity<Map> response = restTemplate.postForEntity(url, product, Map.class);
       
        // Log the response from FakeStore API
        log.info("Product added successfully, response: {}", response.getBody());
        
        return ResponseEntity.ok(response.getBody());
    }

}

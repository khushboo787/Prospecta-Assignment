package com.prospecta.service;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import com.prospecta.entity.Product;
import com.prospecta.exception.AddProductException;

@Service
public class ProductService {

	    @Value("${fakestore.api.url}")
	    private String apiURL;

    private final RestTemplate restTemplate;

    @Autowired
    public ProductService(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public List<Product> getProductsByCategory(String category) {
    	String url = apiURL + "/category/" + category;
        ResponseEntity<Product[]> response = restTemplate.getForEntity(url, Product[].class);  
        return Arrays.asList(response.getBody());
    }
    
    public Product addProduct(Product product) {
        String url = apiURL;
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        
        HttpEntity<Product> request = new HttpEntity<>(product, headers);
        
        ResponseEntity<Product> response = restTemplate.postForEntity(url, request, Product.class);
                if (response.getStatusCode() == HttpStatus.CREATED) {
                // Throw an exception for non-success status codes
                throw new AddProductException("Failed to add product. Status code: " + response.getStatusCode());
            }
       return response.getBody();
         
    }

}

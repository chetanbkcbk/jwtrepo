package com.example.ums.service;

import com.example.ums.entity.Product;
import com.example.ums.repository.ProductRepo;
import com.example.ums.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.apache.coyote.Response;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class ProductService {

    private final ProductRepo productRepo;

    public ResponseEntity<ResponseStructure<Product>> addProduct(Product product) {
        product = productRepo.save(product);
        ResponseStructure<Product> productCreated = ResponseStructure.generate(HttpStatus.CREATED.value(), "Product created", product);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ResponseStructure.generate(HttpStatus.CREATED.value(), "product created", product));
    }
}

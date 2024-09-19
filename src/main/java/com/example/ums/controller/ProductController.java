package com.example.ums.controller;

import com.example.ums.entity.Product;
import com.example.ums.service.ProductService;
import com.example.ums.util.ResponseStructure;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping
@AllArgsConstructor
public class ProductController {

private final ProductService productService;

    @PostMapping ("/products")
    public ResponseEntity<ResponseStructure<Product>> addProduct(@RequestBody Product product){
    return productService.addProduct(product);
    }

}

package com.genspark.product_service.services;

import com.genspark.product_service.entities.Product;

import java.util.List;
import java.util.Optional;

public interface ProductService {
    Product addProduct(Product product);
    Product updateProduct(Product product);
    List<Product> getAllProducts();
    Optional<Product> getProductByID(String id);
    void deleteProduct(String id);

}

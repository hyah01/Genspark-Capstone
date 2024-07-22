package com.genspark.product_service.services;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductServiceImp implements ProductService{
    @Autowired
    private ProductRepository productRepo;

    @Override
    public Product addProduct(Product product) {
        return this.productRepo.save(product);
    }

    @Override
    public Product updateProduct(Product product) {
        return this.productRepo.save(product);
    }

    @Override
    public List<Product> getAllProducts() {
        return this.productRepo.findAll();
    }

    @Override
    public Optional<Product> getProductByID(String id) {
        return this.productRepo.findById(id);
    }

    @Override
    public boolean deleteProduct(String id) {
        if (productRepo.existsById(id)) {
            productRepo.deleteById(id);
            return true;
        } else {
            return false;
        }
    }
}

package com.genspark.product_service.services;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.repositories.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
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
    public Product getProductByID(String id) {
        try {
            return this.productRepo.findById(id).orElseThrow(NoSuchElementException::new);
        }
        catch (Exception e){
            return null;
        }

    }

    @Override
    public Product deleteProduct(String id) {
        this.productRepo.deleteById(id);
        return null;
    }
}

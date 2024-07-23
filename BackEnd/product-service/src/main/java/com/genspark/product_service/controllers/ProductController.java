package com.genspark.product_service.controllers;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/product")
public class ProductController {
    @Autowired
    private ProductService productServ;

    @GetMapping("/all")
    public List<Product> getAllProducts() {
        return this.productServ.getAllProducts();
    }

    @GetMapping("/one/{id}")
    public Optional<Product> getOneProduct(@PathVariable String id) {
        return this.productServ.getProductByID(id);
    }

    @PostMapping("/add")
    public Product addProduct(@RequestBody Product product) {
        return this.productServ.addProduct(product);
    }

    @PutMapping("/update")
    public Product updateProduct(@RequestBody Product product) {
        return this.productServ.updateProduct(product);
    }

    @DeleteMapping("/delete/{id}")
    public Product deleteProduct(@PathVariable String id) {
        return this.productServ.deleteProduct(id);
    }

}

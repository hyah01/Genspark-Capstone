package com.genspark.product_service.controllers;

import com.genspark.product_service.entities.Product;
import com.genspark.product_service.services.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;
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
    public Product getOneProduct(@PathVariable String id) {
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

    @PostMapping("/check-stock")
    public ResponseEntity<?> checkProductStock(@RequestBody Map<String, Object> request) {
        String productId = (String) request.get("productId");
        Integer quantity = (Integer) request.get("quantity");
        Product product = this.productServ.getProductByID(productId);
        if (product != null){
            if (product.getQuantity() >= quantity) {
                return ResponseEntity.ok(Map.of("success", true));
            } else {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(
                        Map.of("success", false, "message", "Not enough stock available")
                );
            }
        } else {
            return ResponseEntity.badRequest().body(
                    Map.of("success", false, "message", "No such product of that ID")
            );
        }
    }
    @PutMapping("/userAdd")
    public ResponseEntity<?> userAddProduct(@RequestHeader(HttpHeaders.AUTHORIZATION)String token, @RequestParam() Object formData) {
        return ResponseEntity.ok(Map.of("success",true));
    }

}

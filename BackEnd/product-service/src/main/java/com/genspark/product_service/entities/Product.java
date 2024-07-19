package com.genspark.product_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    private String id;
    private String sellerId;
    private String name;
    private String description;
    private List<String> category;
    private int quantity;
    private double price;
    private List<String> image;
    private List<String> reviewIds;
    private String promotionId;

}

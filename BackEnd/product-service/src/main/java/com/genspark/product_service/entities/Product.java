package com.genspark.product_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "products")
public class Product {
    @Id
    private String id;
    @Field("seller_id")
    private String sellerId;
    @Field("product_name")
    private String productName;
    @Field("product_description")
    private List<String> productDescription;
    private List<String> category;
    private Integer quantity;
    @Field("price")
    private Price price;
    private List<String> image;
    @Field("review_ids")
    private List<String> reviewIds;
    @Field("promotion_id")
    private String promotionId;

}

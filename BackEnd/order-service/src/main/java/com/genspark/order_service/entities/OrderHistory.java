package com.genspark.order_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "orderHistory")
public class OrderHistory {
    @Id
    private String id;
    private String userId;
    private Map<String, Integer> products;
    private Date orderDate = new Date();
    private Date estimatedDate;
    private Double amount;


}

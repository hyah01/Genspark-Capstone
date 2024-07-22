package com.genspark.order_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "order_history")
public class OrderHistory {
    @Id
    private String id;

    private String userId;
    private List<String> transactionIds;
    private List<String> refundIds;
    private Date orderDate;
    private Date estimatedDate;


}

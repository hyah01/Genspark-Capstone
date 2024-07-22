package com.genspark.order_service.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "order_history")
public class OrderHistory {
    @Id
    private ObjectId id;

    private ObjectId userId;
    private List<ObjectId> transactionIds;
    private List<ObjectId> refundIds;
    private Date orderDate;
    private Date estimatedDate;


}

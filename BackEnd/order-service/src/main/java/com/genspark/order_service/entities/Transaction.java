package com.genspark.order_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "transactions")
public class Transaction {
    @Id
    private ObjectId id;
    private ObjectId userId;
    private ObjectId productId;
    private int quantity;
    private boolean refunded;
}

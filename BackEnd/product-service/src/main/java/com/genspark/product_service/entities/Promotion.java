package com.genspark.product_service.entities;

import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Getter
@Setter
@NoArgsConstructor
@Document(collection = "promotions")
public class Promotion {
    @Id
    private String id;

    private String sellerId;
    private String code;
    private double discount;
    private Date validFrom;
    private Date validTo;

}

package com.genspark.cart_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "saveForLater")
public class SaveForLater {
    @Id
    private String id;
    private String cartId; // To know which User this cartOrder belong to
    private String userId;
    private String productId;


}

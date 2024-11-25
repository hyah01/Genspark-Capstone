package com.genspark.cart_service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Document(collection = "saveForLater")
public class SaveForLaterItems {
    @Id
    private String id;
    private Map<String, SaveForLaterItem> items = new HashMap<>(); // List of cart items


}

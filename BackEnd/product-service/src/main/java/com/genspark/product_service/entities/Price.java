package com.genspark.product_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Price {
    private Double amount;
    private String currency;
    private Saving saving;
}

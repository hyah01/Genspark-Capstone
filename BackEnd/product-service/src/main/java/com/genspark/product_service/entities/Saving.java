package com.genspark.product_service.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class Saving {
    private Double amount;
    private String currency;
    private Double percentage;
}

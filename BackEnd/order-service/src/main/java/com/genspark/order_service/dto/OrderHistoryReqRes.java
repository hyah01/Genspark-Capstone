package com.genspark.order_service.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.genspark.order_service.entities.OrderHistory;
import lombok.Data;
import lombok.Setter;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Data
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
@JsonIgnoreProperties(ignoreUnknown = true)
public class OrderHistoryReqRes {
    // Request fields
    private int statusCode;
    private String message;
    // Fields for single OrderHistory
    private OrderHistory orderHistory;
    // Field for multiple OrderHistory
    private List<OrderHistory> orderHistories;
}


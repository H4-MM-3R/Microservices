package com.aromablossom.OrderService.service;

import com.aromablossom.OrderService.model.OrderRequest;
import com.aromablossom.OrderService.model.OrderResponse;

public interface OrderService {

    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
    
}

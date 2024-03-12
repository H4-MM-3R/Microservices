package com.aromablossom.OrderService.service;

import com.aromablossom.OrderService.model.OrderRequest;

public interface OrderService {

    long placeOrder(OrderRequest orderRequest);
    
}

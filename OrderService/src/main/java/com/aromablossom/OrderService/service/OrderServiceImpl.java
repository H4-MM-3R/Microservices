package com.aromablossom.OrderService.service;

import com.aromablossom.OrderService.entity.Order;
import com.aromablossom.OrderService.model.OrderRequest;
import com.aromablossom.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Override
    public long placeOrder(OrderRequest orderRequest) {

        // Order Entity ---> Save the data with Status Order Created.
        // ProductService ---> Block Products (Reduce the Quantity)
        //
        // +-----------> { SUCCESS } -> Complete
        // |
        // PaymentService ---> Process Payment
        // |
        // +-----------> { FAILURE } -> Cancel

        log.info("Placing Order Request: {}", orderRequest);

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now()).quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);
        
        log.info("Order Placed Successfully with Order Id: {}", order.getId());
        return order.getId();
    }
}

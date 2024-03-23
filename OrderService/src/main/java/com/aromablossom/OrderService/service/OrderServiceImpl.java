package com.aromablossom.OrderService.service;

import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.aromablossom.OrderService.entity.Order;
import com.aromablossom.OrderService.exception.CustomException;
import com.aromablossom.OrderService.external.client.PaymentService;
import com.aromablossom.OrderService.external.client.ProductService;
import com.aromablossom.OrderService.external.request.PaymentRequest;
import com.aromablossom.OrderService.external.response.PaymentResponse;
import com.aromablossom.OrderService.external.response.ProductResponse;
import com.aromablossom.OrderService.model.OrderRequest;
import com.aromablossom.OrderService.model.OrderResponse;
import com.aromablossom.OrderService.repository.OrderRepository;

import lombok.extern.log4j.Log4j2;

@Service
@Log4j2
public class OrderServiceImpl implements OrderService {

    @Autowired
    private OrderRepository orderRepository;

    @Autowired
    private ProductService productService;

    @Autowired
    private PaymentService paymentService;

    @Autowired
    private RestTemplate restTemplate;

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

        productService.reduceQuantity(orderRequest.getProductId(), orderRequest.getQuantity());

        log.info("Creating Order with Status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now()).quantity(orderRequest.getQuantity())
                .build();

        order = orderRepository.save(order);

        log.info("Calling Payment Service to Complete the payment");

        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();

        String OrderStatus = null;

        try {
            paymentService.doPayment(paymentRequest);
            log.info("Payment Completed Successfully, Changing Order Status to PLACED");
            OrderStatus = "PLACED";
        } catch (Exception e) {
            log.error("Payment Failed. Changing Order Status to Failed", e.getMessage());
            OrderStatus = "PAYMENT_FAILED";
        }

        order.setOrderStatus(OrderStatus);
        orderRepository.save(order);
        
        log.info("Order Placed Successfully with Order Id: {}", order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for OrderId: {}", orderId);

        Order order = orderRepository.findById(orderId)
            .orElseThrow(() -> new CustomException("Order not found for the OrderId: " + orderId, "NOT_FOUND", 404));

        log.info("Fetching ProductService for ProductId: {}", order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/" + order.getProductId(), ProductResponse.class);

        log.info("Getting Payment Information from PaymentService");

        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/" + order.getId(), PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
            .productName(productResponse.getProductName())
            .productId(productResponse.getProductId())
            .build();

        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
            .paymentId(paymentResponse.getPaymentId())
            .paymentStatus(paymentResponse.getPaymentStatus())
            .paymentDate(paymentResponse.getPaymentDate())
            .paymentMode(paymentResponse.getPaymentMode())
            .build();

        OrderResponse orderResponse = OrderResponse.builder()
            .orderId(order.getId())
            .orderStatus(order.getOrderStatus())
            .amount(order.getAmount())
            .orderDate(order.getOrderDate())
            .productDetails(productDetails)
            .paymentDetails(paymentDetails)
            .build();

        return orderResponse;
    }
}

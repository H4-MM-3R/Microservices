package com.aromablossom.PaymentService.service;

import com.aromablossom.PaymentService.model.PaymentRequest;
import com.aromablossom.PaymentService.model.PaymentResponse;

public interface PaymentService {

    public long doPayment(PaymentRequest paymentRequest);

    public PaymentResponse getPaymentDetailsByOrderId(String orderId);

    
}

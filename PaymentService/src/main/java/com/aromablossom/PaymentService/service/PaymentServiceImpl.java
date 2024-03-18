package com.aromablossom.PaymentService.service;

import com.aromablossom.PaymentService.entity.TransactionDetails;
import com.aromablossom.PaymentService.model.PaymentMode;
import com.aromablossom.PaymentService.model.PaymentRequest;
import com.aromablossom.PaymentService.model.PaymentResponse;
import com.aromablossom.PaymentService.repository.TransactionDetailsRepository;
import java.time.Instant;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class PaymentServiceImpl implements PaymentService {

  @Autowired private TransactionDetailsRepository transactionDetailsRepository;

  @Override
  public long doPayment(PaymentRequest paymentRequest) {
    log.info("Recording Payment Details: {}", paymentRequest);

    TransactionDetails transactionDetails =
        TransactionDetails.builder()
            .paymentDate(Instant.now())
            .paymentMode(paymentRequest.getPaymentMode().name())
            .paymentStatus("SUCCESS")
            .orderId(paymentRequest.getOrderId())
            .referenceNumber(paymentRequest.getReferenceNumber())
            .amount(paymentRequest.getAmount())
            .build();

    transactionDetailsRepository.save(transactionDetails);

    log.info("Transaction Completed with Id: {}", transactionDetails.getId());
    return transactionDetails.getId();
  }

  @Override
  public PaymentResponse getPaymentDetailsByOrderId(String orderId) {
    log.info("Getting Payment Details for Order Id: {}", orderId);

    TransactionDetails transactionDetails =
        transactionDetailsRepository.findByOrderId(Long.valueOf(orderId));

    return PaymentResponse.builder()
        .paymentId(transactionDetails.getId())
        .paymentMode(PaymentMode.valueOf(transactionDetails.getPaymentMode()))
        .paymentDate(transactionDetails.getPaymentDate())
        .orderId(transactionDetails.getOrderId())
        .status(transactionDetails.getPaymentStatus())
        .amount(transactionDetails.getAmount())
        .build();
  }
}

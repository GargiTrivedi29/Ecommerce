package com.personalproject.PaymentService.service;

import com.personalproject.PaymentService.model.PaymentRequest;
import com.personalproject.PaymentService.model.PaymentResponse;

public interface PaymentService {

    long doPayment(PaymentRequest paymentRequest);

    PaymentResponse getPaymentDetailsByOrderId(String orderId);
}

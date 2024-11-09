package com.personalproject.OrderService.external.client;

import com.personalproject.OrderService.exception.CustomException;
import com.personalproject.OrderService.external.request.PaymentRequest;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;


@CircuitBreaker(name = "external",fallbackMethod = "fallback")
@FeignClient(name = "PAYMENT-SERVICE/payment")
public interface PaymentService {
    @PostMapping()
    public ResponseEntity<Long> doPayment(PaymentRequest paymentRequest);

    default void fallback(Exception e){
        throw new CustomException("Payment Service is not available","UNAVAILABLE",500);
    }
}

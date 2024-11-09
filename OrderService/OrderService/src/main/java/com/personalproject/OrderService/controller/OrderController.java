package com.personalproject.OrderService.controller;

import com.personalproject.OrderService.model.OrderRequest;
import com.personalproject.OrderService.model.OrderResponse;
import com.personalproject.OrderService.service.OrderService;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/order")
@Log4j2
public class OrderController {

    @Autowired
    private OrderService orderservice;

    @PostMapping("/placeOrder")
    public ResponseEntity<Long> placeOrder(@RequestBody OrderRequest orderRequest){
        long orderId = orderservice.placeOrder(orderRequest);
        log.info("Order ID: {}",orderId);
        return new ResponseEntity<>(orderId, HttpStatus.OK);
    }
    @GetMapping("/{orderId}")
    public ResponseEntity<OrderResponse> getOrderDetails(@PathVariable("orderId") long orderId){
        OrderResponse orderResponse = orderservice.getOrderDetails(orderId);
        return new ResponseEntity<>(orderResponse,HttpStatus.OK);
    }

}

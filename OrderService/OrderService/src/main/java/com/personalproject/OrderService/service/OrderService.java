package com.personalproject.OrderService.service;

import com.personalproject.OrderService.model.OrderRequest;
import com.personalproject.OrderService.model.OrderResponse;

public interface OrderService {
    long placeOrder(OrderRequest orderRequest);

    OrderResponse getOrderDetails(long orderId);
}

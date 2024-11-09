package com.personalproject.OrderService.service;

import com.personalproject.OrderService.entity.Order;
import com.personalproject.OrderService.exception.CustomException;
import com.personalproject.OrderService.external.client.PaymentService;
import com.personalproject.OrderService.external.client.ProductService;
import com.personalproject.OrderService.external.request.PaymentRequest;
import com.personalproject.OrderService.external.response.PaymentResponse;
import com.personalproject.OrderService.external.response.ProductResponse;
import com.personalproject.OrderService.model.OrderRequest;
import com.personalproject.OrderService.model.OrderResponse;
import com.personalproject.OrderService.repository.OrderRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;

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
        log.info("Placing order request: {}",orderRequest);

        productService.reduceQuantity(orderRequest.getProductId(),orderRequest.getQuantity());

        log.info("Creating order with status CREATED");

        Order order = Order.builder()
                .amount(orderRequest.getTotalAmount())
                .orderStatus("CREATED")
                .productId(orderRequest.getProductId())
                .orderDate(Instant.now())
                .quantity(orderRequest.getQuantity())
                .build();
        order = orderRepository.save(order);

        log.info("Calling payment service to complete the payment");
        PaymentRequest paymentRequest = PaymentRequest.builder()
                .orderId(order.getId())
                .paymentMode(orderRequest.getPaymentMode())
                .amount(orderRequest.getTotalAmount())
                .build();
        String orderStatus = null;
        try{
            paymentService.doPayment(paymentRequest);
            log.info("Payment completed successfully. Changing the order status to PLACES");
            orderStatus="PLACED";
        }catch(Exception e){
            log.error("Error occured in payment. Changing order status to PAYMENT_FAIL");
            orderStatus = "PAYMENT_FAIL";
        }
        order.setOrderStatus(orderStatus);
        orderRepository.save(order);
        log.info("Ordered placed successfully with order id{}",order.getId());
        return order.getId();
    }

    @Override
    public OrderResponse getOrderDetails(long orderId) {
        log.info("Get order details for order id {}",orderId);
        Order order = orderRepository.findById(orderId).orElseThrow(()-> new CustomException("Order not found for the orderId "+orderId,"NOT_FOUND",404));
        log.info("Invoking product Service to fetch the product for id: {}",order.getProductId());

        ProductResponse productResponse = restTemplate.getForObject("http://PRODUCT-SERVICE/product/"+order.getProductId(), ProductResponse.class);

        log.info("Getting payment details from the payment service");
        PaymentResponse paymentResponse = restTemplate.getForObject("http://PAYMENT-SERVICE/payment/order/"+order.getId(),PaymentResponse.class);

        OrderResponse.ProductDetails productDetails = OrderResponse.ProductDetails.builder()
                .productName(productResponse.getProductName())
                .productId(productResponse.getProductId())
                .quantity(productResponse.getQuantity())
                .price(productResponse.getPrice())
                .build();
        OrderResponse.PaymentDetails paymentDetails = OrderResponse.PaymentDetails.builder()
                .paymentId(paymentResponse.getPaymentId())
                .paymentStatus(paymentResponse.getStatus())
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

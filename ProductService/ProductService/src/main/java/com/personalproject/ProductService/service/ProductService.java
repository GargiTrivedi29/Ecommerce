package com.personalproject.ProductService.service;

import com.personalproject.ProductService.model.ProductRequest;
import com.personalproject.ProductService.model.ProductResponse;

public interface ProductService {
    long addProduct(ProductRequest productRequest);


    ProductResponse getProductProductId(long productId);

    void reduceQuantity(long productId, long quantity);
}

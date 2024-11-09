package com.personalproject.ProductService.service;

import com.personalproject.ProductService.entity.Product;
import com.personalproject.ProductService.exception.ProductServiceCustomException;
import com.personalproject.ProductService.model.ProductRequest;
import com.personalproject.ProductService.model.ProductResponse;
import com.personalproject.ProductService.repository.ProductRepository;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class ProductServiceImpl implements ProductService{

    @Autowired
    private ProductRepository productRepository;
    @Override
    public long addProduct(ProductRequest productRequest) {
        log.info("Adding Product..");
        Product product = Product.builder()
                .productName(productRequest.getName())
                .quantity(productRequest.getQuantity())
                .price(productRequest.getPrice())
                .build();
        productRepository.save(product);
        log.info("Product Created");
        return product.getProductId();
    }

    @Override
    public ProductResponse getProductProductId(long productId) {
        log.info("Get product by productID: {}",productId);
        Product product = productRepository.findById(productId)//findbyid returns optional so we need to throw exception
                 .orElseThrow(()->new ProductServiceCustomException("Product with given id not found","PRODUCT_NOT_FOUND"));
        ProductResponse productResponse = new ProductResponse();
        BeanUtils.copyProperties(product,productResponse);
        return productResponse;
    }

    @Override
    public void reduceQuantity(long productId, long quantity) {
        log.info("Reduce quantity {} for id: {}",quantity,productId);
        Product product = productRepository.findById(productId)
                .orElseThrow(()-> new ProductServiceCustomException("Product with given id not found","PRODUCT NOT FOUND"));
        if(product.getQuantity()<quantity){
            throw new ProductServiceCustomException("Product does not have sufficient quantity","INSUFFICIENT_QUANTITY");

        }
        product.setQuantity((product.getQuantity()-quantity));
        productRepository.save(product);
        log.info("Product quantity updated successfully");
    }
}

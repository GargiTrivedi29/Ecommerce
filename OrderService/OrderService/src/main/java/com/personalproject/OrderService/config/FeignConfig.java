package com.personalproject.OrderService.config;

import com.personalproject.OrderService.external.decoder.CustomErrorDecoder;
import feign.Response;
import feign.codec.ErrorDecoder;
import jakarta.persistence.Id;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class FeignConfig {
    @Bean
    ErrorDecoder errorDecoder(){
        return new CustomErrorDecoder();
    }
}

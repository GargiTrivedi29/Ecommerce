package com.personalproject.ProductService.repository;

import com.personalproject.ProductService.entity.Product;
import com.personalproject.ProductService.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.PostMapping;

@Repository
public interface ProductRepository extends JpaRepository<Product,Long> {


}

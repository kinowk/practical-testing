package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    public List<ProductResponse> getSellingProducts() {
        List<Product> sellingProducts = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return sellingProducts.stream()
                .map(ProductResponse::from)
                .toList();
    }

}

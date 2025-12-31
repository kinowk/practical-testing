package com.example.cafekiosk.spring.api.service.product;

import com.example.cafekiosk.spring.api.controller.product.request.ProductCreateRequest;
import com.example.cafekiosk.spring.api.service.product.response.ProductResponse;
import com.example.cafekiosk.spring.domain.product.Product;
import com.example.cafekiosk.spring.domain.product.ProductRepository;
import com.example.cafekiosk.spring.domain.product.ProductSellingStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {

    private final ProductRepository productRepository;

    @Transactional
    public ProductResponse createProduct(ProductCreateRequest request) {
        String productNumber = createNextProductNumber();

        Product product = request.toEntity(productNumber);
        Product savedProduct = productRepository.save(product);

        return ProductResponse.from(savedProduct);
    }

    @Transactional(readOnly = true)
    public List<ProductResponse> getSellingProducts() {
        List<Product> sellingProducts = productRepository.findAllBySellingStatusIn(ProductSellingStatus.forDisplay());

        return sellingProducts.stream()
                .map(ProductResponse::from)
                .toList();
    }

    @Transactional(readOnly = true)
    private String createNextProductNumber() {
        String latestProductNumber = productRepository.findLatestProductNumber();
        if (latestProductNumber == null) {
            return "001";
        }

        int latestProductNumberInt = Integer.parseInt(latestProductNumber);
        return String.format("%03d", latestProductNumberInt + 1);
    }
}

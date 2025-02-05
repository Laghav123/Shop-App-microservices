package com.triton.product.service;

import com.triton.product.dto.ProductRequest;
import com.triton.product.dto.ProductResponse;
import com.triton.product.model.Product;
import com.triton.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Product createProduct(ProductRequest productRequest){
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();

        productRepository.save(product);
        return product;
    }

    public List<ProductResponse> getAllProducts() {
        List<Product> products =  productRepository.findAll();

        List<ProductResponse> productResponses =  products.stream()
                                                    .map(product ->
                                                            new ProductResponse(
                                                                    product.getId(),
                                                                    product.getName(),
                                                                    product.getDescription(),
                                                                    product.getPrice()
                                                            )
                                                    ).toList();
        return productResponses;
    }
}

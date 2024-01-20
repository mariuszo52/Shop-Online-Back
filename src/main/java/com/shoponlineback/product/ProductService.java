package com.shoponlineback.product;

import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class ProductService {
    private final ProductRepository productRepository;
    private final ProductPagingRepository productPagingRepository;

    public ProductService(ProductRepository productRepository,
                          ProductPagingRepository productPagingRepository) {
        this.productRepository = productRepository;
        this.productPagingRepository = productPagingRepository;
    }

    ProductDto getProductById(long id) {
        Product product = productRepository.findById(id).orElseThrow(() -> new RuntimeException("Cannot find product."));
        return ProductDtoMapper.map(product);
    }

    Page<ProductDto> getAllProducts(Sort sort) {
        List<ProductDto> allProducts = StreamSupport.stream(productPagingRepository.findAll(sort).spliterator(), false)
                .map(ProductDtoMapper::map)
                .collect(Collectors.toList());
        return new PageImpl<>(allProducts);


    }

    public List<ProductDto> getSimilarProducts(Long id) {
        Product product = productRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Product not found."));
        List<Product> allByPlatformName = productRepository.findAllByPlatform_Name(product.getPlatform().getName());
        Collections.shuffle(allByPlatformName);
        return allByPlatformName.stream()
                .limit(5)
                .map(ProductDtoMapper::map)
                .collect(Collectors.toList());

    }
}

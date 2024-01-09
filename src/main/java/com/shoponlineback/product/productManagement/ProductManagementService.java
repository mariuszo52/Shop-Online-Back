package com.shoponlineback.product.productManagement;

import com.shoponlineback.exceptions.product.ProductNotFoundException;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.product.dto.ProductNameUpdateDto;
import com.shoponlineback.product.mapper.ProductDtoMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.StreamSupport;

@Service
public class ProductManagementService {
    private final ProductManagementRepository productManagementRepository;

    public ProductManagementService(ProductManagementRepository productManagementRepository) {
        this.productManagementRepository = productManagementRepository;
    }

    public Page<ProductDto> getAllProducts(int page) {
        int size = 50;
        List<ProductDto> productDtoList = StreamSupport.stream(productManagementRepository.findAll().spliterator(), false)
                .map(ProductDtoMapper::map).toList();
        PageRequest pageRequest = PageRequest.of(page, size);
        List<ProductDto> currentPage = productDtoList.subList(Math.min(page * size, productDtoList.size()),
                Math.min(page * size + size, productDtoList.size()));
        return new PageImpl<>(currentPage, pageRequest, productDtoList.size());
    }
    @Transactional
    public void updateProductName(ProductNameUpdateDto productNameUpdateDto) {
        Product product = productManagementRepository.findById(productNameUpdateDto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        product.setName(productNameUpdateDto.getName());
    }
    @Transactional
    public void updateProductPrice(ProductPriceUpdateDto productPriceUpdateDto) {
        Product product = productManagementRepository.findById(productPriceUpdateDto.getProductId())
                .orElseThrow(ProductNotFoundException::new);
        product.setPrice(productPriceUpdateDto.getPrice());
    }
}

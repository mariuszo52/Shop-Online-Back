package com.shoponlineback.platform;

import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;

@Service
public class PlatformService {
    private final ProductRepository productRepository;

    public PlatformService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    Set<String> getAllPlatformNamesByDevice(String deviceName){
        return productRepository.findAllByPlatform_Device(deviceName).stream()
                .map(product -> product.getPlatform().getName())
                .collect(Collectors.toSet());
    }
}

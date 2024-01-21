package com.shoponlineback.platform;

import com.shoponlineback.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlatformService {
    private final ProductRepository productRepository;
    private final PlatformRepository platformRepository;

    public PlatformService(ProductRepository productRepository, PlatformRepository platformRepository) {
        this.productRepository = productRepository;
        this.platformRepository = platformRepository;
    }

    Set<String> getAllPlatformNamesByDevice(String deviceName){
        return productRepository.findAllByPlatform_Device(deviceName).stream()
                .map(product -> product.getPlatform().getName())
                .collect(Collectors.toSet());
    }
    Set<String> getAllDevicesNames(){
        return StreamSupport.stream(platformRepository.findAll().spliterator(), false)
                .map(Platform::getDevice).collect(Collectors.toSet());
    }
}

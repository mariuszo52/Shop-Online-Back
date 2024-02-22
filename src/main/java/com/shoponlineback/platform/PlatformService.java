package com.shoponlineback.platform;

import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class PlatformService {
    private final PlatformRepository platformRepository;

    public PlatformService(PlatformRepository platformRepository) {
        this.platformRepository = platformRepository;
    }

    Set<String> getAllPlatformNamesByDevice(String deviceName){
        return platformRepository.findAllByDevice(deviceName).stream()
                .map(Platform::getName)
                .collect(Collectors.toSet());
    }
    Set<String> getAllDevicesNames(){
        return StreamSupport.stream(platformRepository.findAll().spliterator(), false)
                .map(Platform::getDevice).collect(Collectors.toSet());
    }
}

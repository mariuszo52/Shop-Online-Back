package com.shoponlineback.platform;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("/platform")
public class PlatformController {
    private final PlatformService platformService;

    public PlatformController(PlatformService platformService) {
        this.platformService = platformService;
    }
    @GetMapping("/device-platforms")
    ResponseEntity<Set<String>> getAllDevicePlatforms(@RequestParam String device){
        return ResponseEntity.ok(platformService.getAllPlatformNamesByDevice(device));
    }
    @GetMapping("/all-devices")
    ResponseEntity<?> getAllDevices(){
        try{
            return ResponseEntity.ok(platformService.getAllDevicesNames());
        }catch (Exception e){
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }
}

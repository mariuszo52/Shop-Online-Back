package com.shoponlineback.platform;

import com.shoponlineback.platform.dto.PlatformDto;
import org.springframework.stereotype.Service;

@Service
public class PlatformDtoMapper {

    public static Platform map(PlatformDto platformDto){
        return new Platform(platformDto.getName(), platformDto.getDevice());
    }
    public static PlatformDto map(Platform platform){
        return new PlatformDto(platform.getName(), platform.getDevice());
    }
}

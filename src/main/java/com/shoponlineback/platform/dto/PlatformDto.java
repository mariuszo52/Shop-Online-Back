package com.shoponlineback.platform.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class PlatformDto {
    private Long id;
    private String name;
    private String device;

    public PlatformDto(String name, String device) {
        this.name = name;
        this.device = device;
    }
}

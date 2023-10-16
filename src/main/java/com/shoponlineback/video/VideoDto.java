package com.shoponlineback.video;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class VideoDto {
    private Long id;
    private String url;
    private Long productId;
}

package com.shoponlineback.product.dto;

import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.language.LanguageDto;
import com.shoponlineback.platform.dto.PlatformDto;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.util.List;


@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class ProductDto {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String name;
    @NotNull
    @Min(0)
    private BigDecimal price;
    @Size(min = 1, max = 20000)
    private String description;
    private String coverImage;
    private List<GenreDto> genres;
    private String releaseDate;
    private String regionalLimitations;
    private String system;
    private Boolean isPreorder;
    @Size(min = 1, max = 5000)
    private String activationDetails;
    private List<LanguageDto> languages;
    private PlatformDto platformDto;
    private String videoUrl;
    private int cartQuantity;
    private Boolean inStock;
    private List<String> screenshotsUrls;
    private Integer sellQuantity;

}


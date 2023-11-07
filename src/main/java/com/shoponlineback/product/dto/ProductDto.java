package com.shoponlineback.product.dto;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.language.LanguageDto;
import com.shoponlineback.platform.dto.PlatformDto;
import com.shoponlineback.systemRequirements.SystemRequirements;
import com.shoponlineback.video.VideoDto;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
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
    private Double price;
    private String description;
    private String coverImage;
    private List<GenreDto> genres;
    private LocalDate releaseDate;
    private Boolean isPreorder;
    private String regionalLimitations;
    private String system;
    private String systemRequirements;
    private String ageRating;
    private String activationDetails;
    private Integer regionId;
    private Boolean isPolishVersion;
    private List<LanguageDto> languages;
    private PlatformDto platformDto;
    private List<String> videoUrls;
    private List<String> screenshotsUrls;
    private Long cartQuantity;

}


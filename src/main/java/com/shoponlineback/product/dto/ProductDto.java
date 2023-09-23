package com.shoponlineback.product.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;


@Getter
@Setter
@Builder
public class ProductDto {
    private Long id;
    @NotNull
    @Size(min = 1)
    private String name;
    @NotNull
    @Min(0)
    private Double price;
    @NotNull
    private String description;
    @NotNull
    private String coverImage;
    @NotNull
    private List<Long> genresIds;
    @NotNull
    private Long platformId;
    @NotNull
    private String releaseDate;
    @NotNull
    private Boolean isPreorder;
    @NotNull
    private String regionalLimitations;
    @NotNull
    private Long systemRequirementsId;
    @NotNull
    private Long ageRatingId;
    @NotNull
    private String ActivationDetails;

}


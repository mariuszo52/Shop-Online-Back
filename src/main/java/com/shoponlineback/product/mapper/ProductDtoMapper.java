package com.shoponlineback.product.mapper;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.systemRequirements.SystemRequirements;

import java.util.List;
import java.util.stream.Collectors;

public class ProductDtoMapper {

    static public Product map(ProductDto productDto){
        List<Genre> genres = productDto.getGenres().stream()
                .map(genreDto -> new Genre(genreDto.getName()))
                .toList();
        return Product.builder()
                .activationDetails(productDto.getActivationDetails())
                .isPolishVersion(productDto.getIsPolishVersion())
                .regionId(productDto.getRegionId())
                .isPreorder(productDto.getIsPreorder())
                .languages(productDto.getLanguages())
                .systemRequirements(new SystemRequirements(productDto.getSystem(), productDto.getSystemRequirements()))
                .ageRating(productDto.getAgeRating())
                .releaseDate(productDto.getReleaseDate())
                .regionalLimitations(productDto.getRegionalLimitations())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .platform(productDto.getPlatform())
                .coverImage(productDto.getCoverImage())
                .description(productDto.getDescription())
                .genres(genres)
                .build();
    }
}

package com.shoponlineback.product.mapper;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.systemRequirements.SystemRequirements;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;
@Service
public class ProductDtoMapper {
    private final GenreRepository genreRepository;

    public ProductDtoMapper(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }

     public Product map(ProductDto productDto){
         List<String> genresNames = productDto.getGenres().stream()
                 .map(GenreDto::getName)
                 .toList();
         List<Genre> genres = genreRepository.findByNameIn(genresNames);
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

package com.shoponlineback.product.mapper;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.platform.PlatformDtoMapper;
import com.shoponlineback.platform.PlatformRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.systemRequirements.SystemRequirements;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDtoMapper {
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final PlatformDtoMapper platformDtoMapper;

    public ProductDtoMapper(GenreRepository genreRepository, PlatformRepository platformRepository, PlatformDtoMapper platformDtoMapper) {
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.platformDtoMapper = platformDtoMapper;
    }

     public Product map(ProductDto productDto){
         List<String> genresNames = productDto.getGenres().stream()
                 .map(GenreDto::getName)
                 .toList();
         List<Genre> genres = genreRepository.findByNameIn(genresNames);
         Platform platform = platformRepository.findByName(productDto.getPlatformDto().getName())
                 .orElse(platformDtoMapper.map(productDto.getPlatformDto()));
         return Product.builder()
                .activationDetails(productDto.getActivationDetails())
                .isPolishVersion(productDto.getIsPolishVersion())
                .regionId(productDto.getRegionId())
                .isPreorder(productDto.getIsPreorder())
              //  .languages(productDto.getLanguages())
                .systemRequirements(new SystemRequirements(productDto.getSystem(), productDto.getSystemRequirements()))
                .ageRating(productDto.getAgeRating())
                .releaseDate(productDto.getReleaseDate())
                .regionalLimitations(productDto.getRegionalLimitations())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .platform(platform)
                .coverImage(productDto.getCoverImage())
                .description(productDto.getDescription())
                .genres(genres)
                .build();
    }
}

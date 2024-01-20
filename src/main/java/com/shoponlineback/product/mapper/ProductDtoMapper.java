package com.shoponlineback.product.mapper;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.genre.GenreMapper;
import com.shoponlineback.genre.GenreRepository;
import com.shoponlineback.language.LanguageMapper;
import com.shoponlineback.language.LanguageRepository;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.platform.PlatformDtoMapper;
import com.shoponlineback.platform.PlatformRepository;
import com.shoponlineback.product.Product;
import com.shoponlineback.product.dto.ProductDto;
import com.shoponlineback.screenshot.Screenshot;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDtoMapper {
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;

    public ProductDtoMapper(GenreRepository genreRepository,
                            PlatformRepository platformRepository) {
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
    }

    public static ProductDto map(Product product) {
        return ProductDto.builder()
                .id(product.getId())
                .name(product.getName())
                .price(product.getPrice())
                .description(product.getDescription())
                .coverImage(product.getCoverImage())
                .genres(product.getGenres().stream().map(GenreMapper::map).toList())
                .releaseDate(product.getReleaseDate())
                .regionalLimitations(product.getRegionalLimitations())
                .activationDetails(product.getActivationDetails())
                .isPreorder(product.getIsPreorder())
                .languages(product.getLanguages().stream().map(LanguageMapper::map).toList())
                .platformDto(PlatformDtoMapper.map(product.getPlatform()))
                .videoUrl(product.getVideoUrl())
                .inStock(product.getInStock())
                .screenshotsUrls(product.getScreenshots().stream().map(Screenshot::getUrl).toList())
                .build();
    }

    public Product map(ProductDto productDto) {
        List<String> genresNames = productDto.getGenres().stream()
                .map(GenreDto::getName)
                .toList();
        List<Genre> genres = genreRepository.findByNameIn(genresNames);
        Platform platform = platformRepository.findByName(productDto.getPlatformDto().getName())
                .orElse(PlatformDtoMapper.map(productDto.getPlatformDto()));

        return Product.builder()
                .id(productDto.getId())
                .activationDetails(productDto.getActivationDetails())
                .isPreorder(productDto.getIsPreorder())
                .releaseDate(productDto.getReleaseDate())
                .regionalLimitations(productDto.getRegionalLimitations())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .platform(platform)
                .coverImage(productDto.getCoverImage())
                .description(productDto.getDescription())
                .genres(genres)
                .inStock(productDto.getInStock())
                .videoUrl(productDto.getVideoUrl())
                .build();
    }


}

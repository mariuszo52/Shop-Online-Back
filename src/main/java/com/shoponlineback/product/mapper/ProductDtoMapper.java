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
import com.shoponlineback.video.Video;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductDtoMapper {
    private final GenreRepository genreRepository;
    private final PlatformRepository platformRepository;
    private final LanguageRepository languageRepository;

    public ProductDtoMapper(GenreRepository genreRepository, PlatformRepository platformRepository, LanguageRepository languageRepository) {
        this.genreRepository = genreRepository;
        this.platformRepository = platformRepository;
        this.languageRepository = languageRepository;
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
                .ageRating(product.getAgeRating())
                .activationDetails(product.getActivationDetails())
                .regionId(product.getRegionId())
                .languages(product.getLanguages().stream().map(LanguageMapper::map).toList())
                .platformDto(PlatformDtoMapper.map(product.getPlatform()))
                .videoUrls(product.getVideos().stream().map(Video::getUrl).toList())
                .screenshotsUrls(product.getScreenshots().stream().map(Screenshot::getUrl).toList())
                .cartQuantity(product.getCartQuantity())
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
                .regionId(productDto.getRegionId())
                .ageRating(productDto.getAgeRating())
                .releaseDate(productDto.getReleaseDate())
                .regionalLimitations(productDto.getRegionalLimitations())
                .name(productDto.getName())
                .price(productDto.getPrice())
                .platform(platform)
                .coverImage(productDto.getCoverImage())
                .description(productDto.getDescription())
                .genres(genres)
                .cartQuantity(productDto.getCartQuantity())
                .build();
    }


}

package com.shoponlineback.product;

import com.shoponlineback.genre.GenreDto;
import com.shoponlineback.product.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final static int ALL_PAGES = 83;


    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    Page<ProductDto> getProducts(@RequestParam(defaultValue = "0") int page,
                                 @RequestParam(defaultValue = "25") int size,
                                 @RequestParam(defaultValue = "") String name,
                                 @RequestParam(defaultValue = "") String device,
                                 @RequestParam(defaultValue = "") String platform,
                                 @RequestParam(defaultValue = "") String genre,
                                 @RequestParam(defaultValue = "") String language,
                                 @RequestParam(required = false) Double minPrice,
                                 @RequestParam(required = false) Double maxPrice)
            throws IOException {
        List<ProductDto> allProducts = productService.getAllProducts(3).stream()
                .filter(productDto -> productDto.getName().toLowerCase().contains(name.toLowerCase()))
                .filter(productDto -> productDto.getPlatformDto().getDevice().contains(device))
                .filter(productDto -> productDto.getPlatformDto().getName().contains(platform))
                .filter(productDto -> genre.isEmpty() || productDto.getGenres().stream().map(GenreDto::getName).toList().contains(genre))
                .filter(productDto -> language.isEmpty() || productDto.getLanguages().contains(language))
                .filter(productDto -> minPrice == null || productDto.getPrice() >= minPrice)
                .filter(productDto -> maxPrice == null || productDto.getPrice() <= maxPrice)
                .collect(Collectors.toList());
        List<ProductDto> currentPage = allProducts.subList(Math.min((page * size), allProducts.size()) ,
                Math.min((page * size + size),allProducts.size()));
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(currentPage, pageRequest, allProducts.size());
    }
}

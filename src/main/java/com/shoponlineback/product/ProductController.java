package com.shoponlineback.product;

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
import java.util.List;
import java.util.stream.Collectors;

@RestController
@CrossOrigin
public class ProductController {
    private final ProductService productService;
    private final static int ALL_PAGES = 620;
    private final static int REGION_EUROPE = 1;
    private final static int REGION_FREE = 3;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/")
    Page<ProductDto> getProducts(@RequestParam(defaultValue = "0") int page, @RequestParam(defaultValue = "25") int size ) throws IOException {
        List<ProductDto> allProducts = productService.getAllProducts(4);
             //   .filter(ProductDto::getIsPolishVersion)
               // .filter(product -> product.getRegionId() == REGION_EUROPE || product.getRegionId() == REGION_FREE)
                //.filter(product -> product.getReleaseDate().isAfter(LocalDate.of(2016,  1, 1)))
               // .collect(Collectors.toList());
        List<ProductDto> currentPage = allProducts.subList(Math.min((page * size), allProducts.size()) , Math.min((page * size + size),allProducts.size()));
        PageRequest pageRequest = PageRequest.of(page, size);
        return new PageImpl<>(currentPage, pageRequest, allProducts.size());
    }
}

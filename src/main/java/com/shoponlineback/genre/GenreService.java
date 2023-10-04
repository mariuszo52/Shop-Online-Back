package com.shoponlineback.genre;

import com.shoponlineback.product.Product;
import com.shoponlineback.product.ProductRepository;
import com.shoponlineback.product.dto.ProductDto;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class GenreService {
    private final ProductRepository productRepository;

    public GenreService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }


    Set<String> getAllGenresNamesByDevice(String device){
        return productRepository.findAllByPlatform_Device(device).stream()
                .flatMap(product -> product.getGenres().stream())
                .map(Genre::getName)
                .collect(Collectors.toCollection(TreeSet::new));

    }
}

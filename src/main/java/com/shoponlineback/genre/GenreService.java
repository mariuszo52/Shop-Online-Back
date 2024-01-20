package com.shoponlineback.genre;

import com.shoponlineback.product.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Collectors;

@Service
public class GenreService {
    private final ProductRepository productRepository;
    private final GenreRepository genreRepository;

    public GenreService(ProductRepository productRepository, GenreRepository genreRepository) {
        this.productRepository = productRepository;
        this.genreRepository = genreRepository;
    }


    Set<String> getAllGenresNamesByDevice(String device){
        return productRepository.findAllByPlatform_Device(device).stream()
                .flatMap(product -> product.getGenres().stream())
                .map(Genre::getName)
                .collect(Collectors.toCollection(TreeSet::new));

    }
}

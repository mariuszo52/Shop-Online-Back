package com.shoponlineback.genre;

import com.shoponlineback.productGenres.ProductGenres;
import org.springframework.stereotype.Service;

import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

@Service
public class GenreService {
    private final GenreRepository genreRepository;

    public GenreService(GenreRepository genreRepository) {
        this.genreRepository = genreRepository;
    }


    Set<String> getAllGenresNames() {
        return StreamSupport.stream(genreRepository.findAll().spliterator(), false)
                .map(Genre::getName)
                .collect(Collectors.toSet());
    }

}

package com.shoponlineback.genre;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Set;
@CrossOrigin
@RestController
@RequestMapping("/genre")
public class GenreController {
    private final GenreService genreService;

    public GenreController(GenreService genreService) {
        this.genreService = genreService;
    }


    @GetMapping("/device-genres")
    ResponseEntity<Set<String>> getAllDeviceGenres(){
        return ResponseEntity.ok(genreService.getAllGenresNames());

    }

}

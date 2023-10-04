package com.shoponlineback.genre;

public class GenreMapper {

    public static GenreDto map(Genre genre){
        return new GenreDto(genre.getName());
    }
}

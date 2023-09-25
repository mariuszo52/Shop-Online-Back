package com.shoponlineback.genre;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class GenreDto {
    private Long id;
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }
}


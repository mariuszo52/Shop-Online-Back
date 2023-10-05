package com.shoponlineback.genre;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
public class GenreDto{
    private Long id;
    private String name;

    public GenreDto(String name) {
        this.name = name;
    }

    }



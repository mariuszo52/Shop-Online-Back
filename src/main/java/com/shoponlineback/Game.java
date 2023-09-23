package com.shoponlineback;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@Setter
@Getter
@ToString
class Game {
    private String name;
    private Long price;
    private String coverImage;
}

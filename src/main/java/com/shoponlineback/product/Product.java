package com.shoponlineback.product;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.language.Language;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.systemRequirements.SystemRequirements;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.time.LocalDate;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Builder
@AllArgsConstructor
@ToString
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 1)
    private String name;
    @NotNull
    @Min(0)
    private Double price;
    private String description;
    private String coverImage;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "product_genres",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "genre_id"))
    private List<Genre> genres;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Platform platform;
    private LocalDate releaseDate;
    private Boolean isPreorder;
    private String regionalLimitations;
    @OneToOne(cascade = CascadeType.PERSIST)
    private SystemRequirements systemRequirements;
    private String ageRating;
    private String activationDetails;
    private Integer regionId;
    private Boolean isPolishVersion;
    @ManyToMany(cascade = CascadeType.PERSIST)
    @JoinTable(
            name = "product_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Language> languages;

}

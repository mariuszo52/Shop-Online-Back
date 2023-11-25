package com.shoponlineback.product;

import com.shoponlineback.cart.Cart;
import com.shoponlineback.genre.Genre;
import com.shoponlineback.language.Language;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.screenshot.Screenshot;
import com.shoponlineback.systemRequirements.SystemRequirements;
import com.shoponlineback.video.Video;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
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
    private BigDecimal price;
    private String description;
    private String coverImage;
    @ManyToMany(fetch = FetchType.EAGER,
            cascade = CascadeType.PERSIST)
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
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "product_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Language> languages;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    private List<Video> videos;
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "product")
    List<Screenshot> screenshots;
    private Long cartQuantity;
    @ManyToMany(mappedBy = "productList")
    List<Cart> carts;
}

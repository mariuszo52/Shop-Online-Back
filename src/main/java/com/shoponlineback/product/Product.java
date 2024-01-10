package com.shoponlineback.product;

import com.shoponlineback.order.Order;
import com.shoponlineback.cart.Cart;
import com.shoponlineback.genre.Genre;
import com.shoponlineback.language.Language;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.screenshot.Screenshot;
import com.shoponlineback.systemRequirements.SystemRequirements;
import com.shoponlineback.user.User;
import com.shoponlineback.video.Video;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

import static jakarta.persistence.CascadeType.*;

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
    @ManyToMany(mappedBy = "products",
            fetch = FetchType.EAGER,
            cascade = {PERSIST})
    private List<Genre> genres;
    @OneToOne(cascade = PERSIST)
    private Platform platform;
    private LocalDate releaseDate;
    private Boolean isPreorder;
    private String regionalLimitations;
    @OneToOne(cascade = {PERSIST, REMOVE})
    private SystemRequirements systemRequirements;
    private String ageRating;
    private String activationDetails;
    private Integer regionId;
    private Boolean isPolishVersion;
    @ManyToMany(mappedBy = "products",
            fetch = FetchType.EAGER)
    private List<Language> languages;
    @OneToMany(fetch = FetchType.EAGER,
            cascade = REMOVE,
            mappedBy = "product")
    private List<Video> videos;
    @OneToMany(fetch = FetchType.EAGER,
            cascade = REMOVE,
            mappedBy = "product")
    List<Screenshot> screenshots;
    private Long cartQuantity;
    @ManyToMany(mappedBy = "productList")
    private List<Cart> carts;
    @ManyToMany(mappedBy = "productList")
    private List<Order> orderList;
    @ManyToMany(mappedBy = "favoriteProducts")
    private List<User> users;
}

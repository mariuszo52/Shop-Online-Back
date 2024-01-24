package com.shoponlineback.product;

import com.shoponlineback.order.Order;
import com.shoponlineback.cart.Cart;
import com.shoponlineback.genre.Genre;
import com.shoponlineback.language.Language;
import com.shoponlineback.platform.Platform;
import com.shoponlineback.screenshot.Screenshot;
import com.shoponlineback.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
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
    @Size(min = 1, max = 20000)
    private String description;
    private String coverImage;
    @ManyToMany(mappedBy = "products",
            fetch = FetchType.EAGER,
            cascade = {PERSIST})
    private List<Genre> genres;
    @OneToOne
    private Platform platform;
    private String releaseDate;
    private String regionalLimitations;
    private Boolean inStock;
    private Boolean isPreorder;
    @Size(min = 1, max = 5000)
    private String activationDetails;
    @ManyToMany(mappedBy = "products",
            fetch = FetchType.EAGER)
    private List<Language> languages;
    private String videoUrl;
    private int sellQuantity;
    @OneToMany(fetch = FetchType.EAGER,
            cascade = {MERGE,REMOVE},
            mappedBy = "product")
    List<Screenshot> screenshots;
    @ManyToMany(mappedBy = "productList")
    private List<Cart> carts;
    @ManyToMany(mappedBy = "productList")
    private List<Order> orderList;
    @ManyToMany(mappedBy = "favoriteProducts")
    private List<User> users;
}

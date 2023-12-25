package com.shoponlineback.productGenres;

import com.shoponlineback.genre.Genre;
import com.shoponlineback.product.Product;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ProductGenres {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "genre_id")
    private Genre genre;

    public ProductGenres(Product product, Genre genre) {
        this.product = product;
        this.genre = genre;
    }
}

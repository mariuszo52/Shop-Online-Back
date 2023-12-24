package com.shoponlineback.productLanguage;

import com.shoponlineback.language.Language;
import com.shoponlineback.product.Product;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor
@Getter
public class ProductLanguage {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "language_id")
    private Language language;

    public ProductLanguage(Product product, Language language) {
        this.product = product;
        this.language = language;
    }
}

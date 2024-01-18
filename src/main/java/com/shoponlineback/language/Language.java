package com.shoponlineback.language;

import com.shoponlineback.product.Product;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@AllArgsConstructor
public class Language {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String iconUrl;
    @ManyToMany
    @JoinTable(
            name = "product_language",
            joinColumns = @JoinColumn(name = "language_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    List<Product> products;

    public Language(String name, String iconUrl) {
        this.name = name;
        this.iconUrl = iconUrl;
    }

    @Override
    public String toString() {
        return "Language{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", iconUrl='" + iconUrl + '\'' +
                '}';
    }
}


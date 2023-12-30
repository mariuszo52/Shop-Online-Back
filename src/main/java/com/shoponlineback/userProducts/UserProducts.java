package com.shoponlineback.userProducts;

import com.shoponlineback.product.Product;
import com.shoponlineback.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@NoArgsConstructor
@Getter
@Table(name = "favorite_products")
public class UserProducts {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    private LocalDateTime addTime;

    public UserProducts(Product product, User user, LocalDateTime addTime) {
        this.product = product;
        this.user = user;
        this.addTime = addTime;
    }
}

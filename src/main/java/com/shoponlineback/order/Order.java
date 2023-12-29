package com.shoponlineback.order;

import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.Product;
import com.shoponlineback.shippingAddress.ShippingAddress;
import com.shoponlineback.user.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@Setter
@Table(name = "orders")
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne
    private User user;
    @OneToOne
    private ShippingAddress shippingAddress;
    @Enumerated(EnumType.STRING)
    private PaymentMethod paymentMethod;
    @ManyToMany
    @JoinTable(name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;
    private BigDecimal totalPrice;
}

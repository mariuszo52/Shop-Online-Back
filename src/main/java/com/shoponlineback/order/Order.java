package com.shoponlineback.order;

import com.shoponlineback.paymentMethod.PaymentMethod;
import com.shoponlineback.product.Product;
import com.shoponlineback.shippingAddress.ShippingAddress;
import com.shoponlineback.user.User;
import jakarta.persistence.*;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@NoArgsConstructor
@Getter
@AllArgsConstructor
@Builder
@Table(name = "orders")
@Setter
public class Order {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @OneToOne
    @NotNull
    private User user;
    @OneToOne(cascade = CascadeType.PERSIST)
    @NotNull
    private ShippingAddress shippingAddress;
    @Enumerated(EnumType.STRING)
    @NotNull
    private PaymentMethod paymentMethod;
    @ManyToMany
    @JoinTable(name = "order_products",
            joinColumns = @JoinColumn(name = "order_id"),
            inverseJoinColumns = @JoinColumn(name = "product_id"))
    private List<Product> productList;
    @NotNull
    @Min(1)
    private BigDecimal totalPrice;
    @Enumerated(EnumType.STRING)
    @NotNull
    private OrderStatus orderStatus;
    @NotNull
    private LocalDateTime orderDate;
}

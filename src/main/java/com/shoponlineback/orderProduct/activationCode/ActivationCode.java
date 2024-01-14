package com.shoponlineback.orderProduct.activationCode;

import com.shoponlineback.orderProduct.OrderProduct;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ActivationCode {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String code;
    @ManyToOne
    @JoinColumn(name = "order_product_id")
    private OrderProduct orderProduct;

    public ActivationCode(String code, OrderProduct orderProduct) {
        this.code = code;
        this.orderProduct = orderProduct;
    }
}

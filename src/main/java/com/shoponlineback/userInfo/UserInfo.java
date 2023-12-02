package com.shoponlineback.userInfo;

import com.shoponlineback.shippingAddress.ShippingAddress;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.util.List;

import static jakarta.persistence.CascadeType.*;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UserInfo {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotNull
    @Size(min = 2)
    private String name;
    @NotNull
    @Size(min = 2)
    private String lastName;
    @OneToOne(cascade = {PERSIST, REMOVE})
    private ShippingAddress shippingAddress;


    public UserInfo(String name, String lastName, ShippingAddress shippingAddress) {
        this.name = name;
        this.lastName = lastName;
        this.shippingAddress = shippingAddress;
    }
}


package com.shoponlineback.userInfo;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

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
    @Size(min = 2)
    private String address;
    @Size(min = 2)
    private String city;
    @Size(min = 2)
    private String country;
    @Size(min = 5, max = 5)
    private String postalCode;
    @Size(min = 9, max = 9)
    private String phoneNumber;

    public UserInfo(String name, String lastName) {
        this.name = name;
        this.lastName = lastName;
    }
}


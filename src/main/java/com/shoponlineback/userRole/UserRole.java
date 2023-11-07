package com.shoponlineback.userRole;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@NoArgsConstructor
@Getter
@Setter
public class UserRole {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    @NotNull
    private Long id;
    private String name;
}

package com.shoponlineback.user;

import com.shoponlineback.cart.Cart;
import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.*;
import lombok.*;
import org.hibernate.annotations.Cascade;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

import static org.hibernate.annotations.CascadeType.PERSIST;

@Entity
@ToString
@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User implements UserDetails {
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Id
    private Long id;
    @NotNull
    @Size(min = 1, max = 30)
    @Pattern(regexp = "\\S+", message = "Username needs to have 1-30 characters without blank spaces.")
    private String username;
    @NotNull
    @Email
    private String email;
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;
    @NotNull
    @OneToOne
    private UserRole userRole;
    @NotNull
    @OneToOne
    @Cascade(PERSIST)
    private UserInfo userInfo;
    @NotNull
    private Boolean isEnabled;
    @Size(min = 40, max = 40)
    private String activationToken;
    @OneToOne(cascade = CascadeType.PERSIST)
    private Cart cart;

    public User(String username, String email, UserRole userRole, UserInfo userInfo, Boolean isEnabled, Cart cart) {
        this.username = username;
        this.email = email;
        this.userRole = userRole;
        this.userInfo = userInfo;
        this.isEnabled = isEnabled;
        this.cart = cart;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority simpleGrantedAuthority = new SimpleGrantedAuthority(userRole.getName());
        return Collections.singleton(simpleGrantedAuthority);
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return isEnabled;
    }
}

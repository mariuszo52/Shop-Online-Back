package com.shoponlineback.user;

import com.shoponlineback.userInfo.UserInfo;
import com.shoponlineback.userRole.UserRole;
import jakarta.persistence.*;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

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
    private String username;
    @NotNull
    @Email
    private String email;
    @NotNull
    @Pattern(regexp = "^(?=.*[A-Z])(?=.*\\d)(?=.*[\\W_]).{8,}$")
    private String password;
    @NotNull
    @OneToOne
    private UserRole userRole;
    @NotNull
    @OneToOne
    private UserInfo userInfo;

    public User(Long id, String username, String email, UserRole userRole) {
        this.id = id;
        this.username = username;
        this.email = email;
        this.userRole = userRole;
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
        return true;
    }
}

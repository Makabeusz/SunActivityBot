package com.sojka.sunactivity.security.user;

import com.google.cloud.firestore.annotation.Exclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class User implements UserDetails {

    private String email;
    private String firstname;
    private String lastname;
    private String password;
    private Role role;

    @Override
    @Exclude
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.roleName()));
    }

    @Override
    public String getPassword() {
        return password;
    }

    @Override
    @Exclude
    public String getUsername() {
        return email;
    }

    @Override
    @Exclude
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    @Exclude
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    @Exclude
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    @Exclude
    public boolean isEnabled() {
        return true;
    }
}

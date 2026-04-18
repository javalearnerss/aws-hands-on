package com.learn.microservices.trading.auth.server.entity;

import com.learn.microservices.trading.auth.server.identity.entity.Role;
import com.learn.microservices.trading.auth.server.identity.entity.TradingUser;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TradingUserDetails implements UserDetails {

    private final TradingUser user;

    public TradingUserDetails(TradingUser user){
        this.user = user;
    }

    public String getUserId(){
        return user.getId();
    }

    public String getEmail(){
        return user.getEmail();
    }

    public String getTenantId(){
        return user.getTenantId();
    }


    public Set<String> getRoles(){
        return user.getRoles()
                .stream()
                .map(Role::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities(){
        return user.getRoles()
                .stream()
                .map(r -> new SimpleGrantedAuthority(r.getName()))
                .toList();
    }

    public List<String> getAudience(){
        return user.getAudiences().stream().map(audience -> audience.getName()).collect(Collectors.toUnmodifiableList());
    }

    public String getScope(){
        return user.getRoles().stream().map(role -> role.getScopes().stream().map(scope -> scope.getName()).collect(Collectors.joining(","))).collect(Collectors.joining(","));
    }

    @Override
    public String getPassword(){
        return user.getPassword();
    }

    @Override
    public String getUsername(){
        return user.getUsername();
    }

    @Override
    public boolean isEnabled(){
        return user.isEnabled();
    }

    @Override public boolean isAccountNonExpired(){ return true; }
    @Override public boolean isAccountNonLocked(){ return true; }
    @Override public boolean isCredentialsNonExpired(){ return true; }
}
package com.learn.microservices.trading.auth.server.service;


import com.learn.microservices.trading.auth.server.entity.TradingUserDetails;
import com.learn.microservices.trading.auth.server.identity.entity.TradingUser;
import com.learn.microservices.trading.auth.server.repository.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    private final UserRepository userRepo;

    public UserDetailsServiceImpl(UserRepository userRepo){
        this.userRepo=userRepo;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        TradingUser user = userRepo.findByUsername(username).orElseThrow();
        return new TradingUserDetails(user);
    }

}
package com.learn.microservices.trading.auth.server.repository;


import com.learn.microservices.trading.auth.server.identity.entity.TradingUser;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<TradingUser,String> {
    Optional<TradingUser> findByUsername(String username);
}
package com.learn.microservices.trading.auth.server.repository;


import com.learn.microservices.trading.auth.server.entity.KeyConfig;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface KeyConfigRepository
        extends JpaRepository<KeyConfig, Integer> {

    @Query("select k.activeKid from KeyConfig k where k.id = 1")
    String findActiveKid();
}
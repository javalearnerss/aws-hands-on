package com.learn.microservices.trading.auth.server.repository;


import com.learn.microservices.trading.auth.server.identity.entity.Role;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RoleRepository extends JpaRepository<Role,String> {}
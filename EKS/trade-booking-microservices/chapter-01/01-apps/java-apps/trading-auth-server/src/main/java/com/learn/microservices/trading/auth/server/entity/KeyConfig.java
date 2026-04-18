package com.learn.microservices.trading.auth.server.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "jwt_key_config")
@Setter
@Getter
public class KeyConfig {

    @Id
    private Integer id;

    @Column(name = "active_kid", nullable = false)
    private String activeKid;

}
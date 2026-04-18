package com.learn.microservices.trading.auth.server.identity.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Entity
@Table(name = "scopes")
@Getter
@Setter
@ToString
public class Scope {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

}

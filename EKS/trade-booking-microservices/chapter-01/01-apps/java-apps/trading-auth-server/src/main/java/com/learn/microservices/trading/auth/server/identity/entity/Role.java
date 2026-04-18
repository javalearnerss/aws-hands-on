package com.learn.microservices.trading.auth.server.identity.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Set;

@Entity
@Table(name="roles")
@Getter
@Setter
@ToString
public class Role {

    @Id
    @GeneratedValue
    private Long id;

    private String name;


    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name="role_scopes",
            joinColumns = @JoinColumn(name="role_id"),
            inverseJoinColumns = @JoinColumn(name="scope_id")
    )
    private Set<Scope> scopes;

}
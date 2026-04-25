package com.learn.microservices.refdata.provider.entity;


import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Table(name = "ref_data")
@Entity
@Getter
@Setter
@ToString
public class ReferenceData {

    @Id
    @GeneratedValue
    private Long id;

    private String symbol;

    private String isin;

}

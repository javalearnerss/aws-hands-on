package com.learn.microservices.refdata.provider.repository;

import com.learn.microservices.refdata.provider.entity.ReferenceData;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ReferenceDataRepository extends JpaRepository<ReferenceData, Long> {

    public Optional<ReferenceData> findBySymbol(String symbol);

}

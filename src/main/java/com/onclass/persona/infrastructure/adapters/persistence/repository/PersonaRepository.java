package com.onclass.persona.infrastructure.adapters.persistence.repository;

import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import reactor.core.publisher.Mono;

public interface PersonaRepository extends ReactiveCrudRepository<PersonaEntity, Long> {
    Mono<PersonaEntity> findById(Long id);
}
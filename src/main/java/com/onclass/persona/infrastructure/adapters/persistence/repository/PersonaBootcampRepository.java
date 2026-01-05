package com.onclass.persona.infrastructure.adapters.persistence.repository;

import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface PersonaBootcampRepository extends ReactiveCrudRepository<PersonaBootcampEntity, Long> {
    Flux<PersonaBootcampEntity> findByPersonaId(Long personaId);

    Flux<PersonaBootcampEntity> findByBootcampId(Long bootcampId);
    
    Flux<PersonaBootcampEntity> findByPersonaIdAndBootcampId(Long personaId, Long bootcampId);
}


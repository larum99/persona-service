package com.onclass.persona.infrastructure.adapters.persistence;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaRepository;
import reactor.core.publisher.Mono;

public class PersonaPersistenceAdapter implements PersonaPersistencePort {

    private final PersonaRepository repository;
    private final PersonaEntityMapper mapper;

    public PersonaPersistenceAdapter(PersonaRepository repository, PersonaEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<Persona> obtenerPersonaPorId(Long personaId) {
        return repository.findById(personaId)
                .map(mapper::toModel);
    }
}
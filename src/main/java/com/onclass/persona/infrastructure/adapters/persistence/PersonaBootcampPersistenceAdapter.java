package com.onclass.persona.infrastructure.adapters.persistence;


import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaBootcampEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaBootcampRepository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public class PersonaBootcampPersistenceAdapter implements PersonaBootcampPersistencePort {

    private final PersonaBootcampRepository repository;
    private final PersonaBootcampEntityMapper mapper;

    public PersonaBootcampPersistenceAdapter(PersonaBootcampRepository repository,
                                             PersonaBootcampEntityMapper mapper) {
        this.repository = repository;
        this.mapper = mapper;
    }

    @Override
    public Mono<PersonaBootcamp> savePersonaBootcamp(PersonaBootcamp relacion) {
        PersonaBootcampEntity entity = mapper.toEntity(relacion);
        return repository.save(entity)
                .map(mapper::toModel);
    }

    @Override
    public Flux<PersonaBootcamp> findBootcampsByPersonaId(Long personaId) {
        return repository.findByPersonaId(personaId)
                .map(mapper::toModel);
    }

    @Override
    public Flux<PersonaBootcamp> findPersonasByBootcampId(Long bootcampId) {
        return repository.findByBootcampId(bootcampId)
                .map(mapper::toModel);
    }
}

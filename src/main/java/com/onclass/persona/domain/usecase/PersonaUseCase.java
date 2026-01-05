package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import reactor.core.publisher.Mono;

public class PersonaUseCase implements PersonaServicePort {

    private final PersonaPersistencePort persistencePort;

    public PersonaUseCase(PersonaPersistencePort persistencePort) {
        this.persistencePort = persistencePort;
    }

    @Override
    public Mono<Persona> obtenerPersonaPorId(Long personaId) {
        return persistencePort.obtenerPersonaPorId(personaId);
    }
}
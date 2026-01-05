package com.onclass.persona.domain.spi;

import com.onclass.persona.domain.model.Persona;
import reactor.core.publisher.Mono;

public interface PersonaPersistencePort {
    Mono<Persona> obtenerPersonaPorId(Long personaId);
}
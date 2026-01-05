package com.onclass.persona.domain.api;

import com.onclass.persona.domain.model.Persona;
import reactor.core.publisher.Mono;

public interface PersonaServicePort {
    Mono<Persona> obtenerPersonaPorId(Long personaId);
}
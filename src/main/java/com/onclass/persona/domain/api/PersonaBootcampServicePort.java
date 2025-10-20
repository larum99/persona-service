package com.onclass.persona.domain.api;

import reactor.core.publisher.Mono;

public interface PersonaBootcampServicePort {
    Mono<Void> registrarPersonaBootcamp(Long personaId, Long bootcampId);
}

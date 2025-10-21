package com.onclass.persona.domain.spi;

import com.onclass.persona.domain.model.PersonaBootcamp;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface PersonaBootcampPersistencePort {
    Mono<PersonaBootcamp> savePersonaBootcamp(PersonaBootcamp relacion);
    Flux<PersonaBootcamp> findBootcampsByPersonaId(Long personaId);
}

package com.onclass.persona.domain.api;

import com.onclass.persona.domain.model.PersonaBootcamp;
import reactor.core.publisher.Flux;

import java.util.List;

public interface PersonaBootcampServicePort {
    Flux<PersonaBootcamp> inscribirPersonaEnBootcamps(List<PersonaBootcamp> relaciones, String messageId);
    Flux<PersonaBootcamp> obtenerPersonasPorBootcampId(Long bootcampId, String messageId);

}

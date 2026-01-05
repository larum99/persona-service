package com.onclass.persona.infrastructure.entrypoints.handler;

import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.infrastructure.entrypoints.utils.HandlerConstants;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;

@Component
public class PersonaHandlerImpl {

    private final PersonaServicePort servicePort;

    public PersonaHandlerImpl(PersonaServicePort servicePort) {
        this.servicePort = servicePort;
    }

    public Mono<ServerResponse> obtenerPersonaPorId(ServerRequest request) {
        Long personaId = Long.valueOf(request.pathVariable(HandlerConstants.PERSONA_ID_PATH_VARIABLE));
        
        return servicePort.obtenerPersonaPorId(personaId)
                .flatMap(persona -> ServerResponse.ok().bodyValue(persona))
                .switchIfEmpty(ServerResponse.status(HttpStatus.NOT_FOUND).build());
    }
}
package com.onclass.persona.infrastructure.entrypoints;

import com.onclass.persona.infrastructure.entrypoints.handler.PersonaBootcampHandlerImpl;
import com.onclass.persona.infrastructure.entrypoints.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    //@PersonaBootcampApiDoc
    public RouterFunction<ServerResponse> personaBootcampRoutes(PersonaBootcampHandlerImpl handler) {
        return route(POST(Constants.PERSONA_BOOTCAMP_PATH), handler::inscribirPersonaEnBootcamps);
    }
}

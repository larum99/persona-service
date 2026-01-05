package com.onclass.persona.infrastructure.entrypoints;

import com.onclass.persona.application.configSwagger.PersonaBootcampGetApiDoc;
import com.onclass.persona.application.configSwagger.PersonaBootcampInscribirApiDoc;
import com.onclass.persona.application.configSwagger.PersonaGetApiDoc;
import com.onclass.persona.infrastructure.entrypoints.handler.PersonaBootcampHandlerImpl;
import com.onclass.persona.infrastructure.entrypoints.handler.PersonaHandlerImpl;
import com.onclass.persona.infrastructure.entrypoints.utils.Constants;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.server.RouterFunction;
import org.springframework.web.reactive.function.server.ServerResponse;

import static org.springframework.web.reactive.function.server.RequestPredicates.GET;
import static org.springframework.web.reactive.function.server.RequestPredicates.POST;
import static org.springframework.web.reactive.function.server.RouterFunctions.route;

@Configuration
public class RouterRest {

    @Bean
    @PersonaBootcampInscribirApiDoc
    public RouterFunction<ServerResponse> personaBootcampInscribirRoutes(PersonaBootcampHandlerImpl handler) {
        return route(POST(Constants.PERSONA_BOOTCAMP_PATH), handler::inscribirPersonaEnBootcamps);
    }

    @Bean
    @PersonaBootcampGetApiDoc
    public RouterFunction<ServerResponse> personaBootcampGetRoutes(PersonaBootcampHandlerImpl handler) {
        return route(GET(Constants.PERSONA_BOOTCAMP_PATH + Constants.BOOTCAMP_ID_SEGMENT),
                        handler::obtenerPersonasPorBootcampId);
    }

    @Bean
    @PersonaGetApiDoc
    public RouterFunction<ServerResponse> personaRoutes(PersonaHandlerImpl handler) {
        return route(GET(Constants.PERSONAS_ID_PATH), handler::obtenerPersonaPorId);
    }
}

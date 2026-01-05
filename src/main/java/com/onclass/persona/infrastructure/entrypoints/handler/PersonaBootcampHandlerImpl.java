package com.onclass.persona.infrastructure.entrypoints.handler;

import com.onclass.persona.domain.api.PersonaBootcampServicePort;
import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.exceptions.TechnicalException;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaBootcampDTO;
import com.onclass.persona.infrastructure.entrypoints.mapper.PersonaBootcampMapper;
import com.onclass.persona.infrastructure.entrypoints.utils.APIResponse;
import com.onclass.persona.infrastructure.entrypoints.utils.Constants;
import com.onclass.persona.infrastructure.entrypoints.utils.HandlerConstants;
import com.onclass.persona.infrastructure.entrypoints.utils.ErrorDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.server.ServerRequest;
import org.springframework.web.reactive.function.server.ServerResponse;
import reactor.core.publisher.Mono;
import reactor.util.context.Context;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Component
public class PersonaBootcampHandlerImpl {

    private static final Logger log = LoggerFactory.getLogger(PersonaBootcampHandlerImpl.class);

    private final PersonaBootcampServicePort servicePort;
    private final PersonaBootcampMapper mapper;

    public PersonaBootcampHandlerImpl(PersonaBootcampServicePort servicePort,
                                      PersonaBootcampMapper mapper) {
        this.servicePort = servicePort;
        this.mapper = mapper;
    }

    public Mono<ServerResponse> inscribirPersonaEnBootcamps(ServerRequest request) {
        String messageId = UUID.randomUUID().toString();

        return request.bodyToFlux(PersonaBootcampDTO.class)
                .map(mapper::toModel)
                .collectList()
                .flatMapMany(list -> servicePort.inscribirPersonaEnBootcamps(list, messageId))
                .collectList()
                .flatMap(saved -> {
                    List<PersonaBootcampDTO> savedDTOs = saved.stream()
                            .map(mapper::toDTO)
                            .toList();
                    APIResponse response = APIResponse.builder()
                            .code(TechnicalMessage.PERSONA_BOOTCAMP_REGISTERED.getCode())
                            .message(TechnicalMessage.PERSONA_BOOTCAMP_REGISTERED.getDescription())
                            .identifier(messageId)
                            .date(Instant.now().toString())
                            .data(savedDTOs)
                            .build();
                    return ServerResponse.status(HttpStatus.CREATED).bodyValue(response);
                })
                .onErrorResume(ex -> buildErrorResponse(messageId, ex));
    }

    public Mono<ServerResponse> obtenerPersonasPorBootcampId(ServerRequest request) {
        String messageId = getMessageId(request);
        Long bootcampId = Long.valueOf(request.pathVariable(HandlerConstants.BOOTCAMP_ID_PATH_VARIABLE));

        return servicePort.obtenerPersonasPorBootcampId(bootcampId, messageId)
                .collectList()
                .flatMap(personas -> ServerResponse.ok().bodyValue(personas))
                .onErrorResume(ex -> buildErrorResponse(messageId, ex));
    }


    private Mono<ServerResponse> buildErrorResponse(String messageId, Throwable ex) {
        if (ex instanceof BusinessException bex) {
            // Casos específicos que requieren 404
            if (bex.getTechnicalMessage() == TechnicalMessage.BOOTCAMP_NOT_FOUND ||
                bex.getTechnicalMessage() == TechnicalMessage.PERSONA_NOT_FOUND ||
                bex.getTechnicalMessage() == TechnicalMessage.NO_PERSONAS_FOUND) {
                return buildError(HttpStatus.NOT_FOUND, messageId, bex.getTechnicalMessage());
            }
            return buildError(HttpStatus.BAD_REQUEST, messageId, bex.getTechnicalMessage());
        }
        if (ex instanceof TechnicalException tex) {
            return buildError(HttpStatus.INTERNAL_SERVER_ERROR, messageId, tex.getTechnicalMessage());
        }
        return buildError(HttpStatus.INTERNAL_SERVER_ERROR, messageId, TechnicalMessage.INTERNAL_ERROR);
    }

    private Mono<ServerResponse> buildError(HttpStatus status, String identifier, TechnicalMessage error) {
        APIResponse apiErrorResponse = APIResponse.builder()
                .code(error.getCode())
                .message(error.getDescription())
                .identifier(identifier)
                .date(Instant.now().toString())
                .errors(List.of(ErrorDTO.builder()
                        .code(error.getCode())
                        .message(error.getDescription())
                        .param(error.getParam())
                        .build()))
                .build();
        return ServerResponse.status(status).bodyValue(apiErrorResponse);
    }

    private String getMessageId(ServerRequest serverRequest) {
        return Optional.ofNullable(serverRequest.headers().firstHeader(Constants.X_MESSAGE_ID))
                .orElse(UUID.randomUUID().toString());
    }
}

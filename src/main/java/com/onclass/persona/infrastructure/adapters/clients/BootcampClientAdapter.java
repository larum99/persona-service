package com.onclass.persona.infrastructure.adapters.clients;

import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.utils.BootcampSummary;
import com.onclass.persona.infrastructure.adapters.utils.ClientConstants;
import com.onclass.persona.infrastructure.entrypoints.dto.BootcampSummaryDTO;
import com.onclass.persona.infrastructure.entrypoints.utils.Constants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.time.LocalDate;

@Component
public class BootcampClientAdapter implements BootcampClientPort {

    private final WebClient webClient;

    public BootcampClientAdapter(WebClient.Builder webClientBuilder,
                                 @Value(ClientConstants.BOOTCAMP_SERVICE_URL_PROPERTY) String bootcampUrl) {
        this.webClient = webClientBuilder
                .baseUrl(bootcampUrl)
                .build();
    }

    @Override
    public Mono<BootcampSummary> obtenerBootcampPorId(Long bootcampId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path(ClientConstants.BOOTCAMPS_PATH)
                        .build(bootcampId))
                .header(Constants.X_MESSAGE_ID, ClientConstants.DEFAULT_MESSAGE_ID)
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> response.bodyToMono(String.class)
                                .flatMap(body -> {
                                    if (response.statusCode().value() == ClientConstants.HTTP_NOT_FOUND) {
                                        return Mono.error(new RuntimeException(ClientConstants.NOT_FOUND_ERROR));
                                    }
                                    return Mono.error(new RuntimeException(ClientConstants.CLIENT_ERROR_PREFIX + response.statusCode()));
                                }))
                .bodyToMono(BootcampSummaryDTO.class)
                .map(dto -> {
                    LocalDate fechaInicio = dto.fechaLanzamiento();
                    LocalDate fechaFin = dto.fechaLanzamiento().plusWeeks(dto.duracion());
                    return new BootcampSummary(dto.id(), dto.nombre(), dto.descripcion(), dto.fechaLanzamiento(), dto.duracion());
                });
    }
}

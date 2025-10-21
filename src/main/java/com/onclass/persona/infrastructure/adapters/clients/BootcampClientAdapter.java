package com.onclass.persona.infrastructure.adapters.clients;

import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.utils.BootcampSummary;
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
                                 @Value("${services.bootcamp.url}") String bootcampUrl) {
        this.webClient = webClientBuilder
                .baseUrl(bootcampUrl)
                .build();
    }

    @Override
    public Mono<BootcampSummary> obtenerBootcampPorId(Long bootcampId) {
        return webClient.get()
                .uri(uriBuilder -> uriBuilder
                        .path("/bootcamps/{bootcampId}")
                        .build(bootcampId))
                .header(Constants.X_MESSAGE_ID, "12345")
                .retrieve()
                .bodyToMono(BootcampSummaryDTO.class)
                .map(dto -> {
                    LocalDate fechaInicio = dto.fechaLanzamiento();
                    LocalDate fechaFin = dto.fechaLanzamiento().plusWeeks(dto.duracion());
                    return new BootcampSummary(dto.id(), dto.nombre(), dto.descripcion(), dto.fechaLanzamiento(), dto.duracion());
                });
    }
}

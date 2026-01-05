package com.onclass.persona.infrastructure.adapters.clients;

import com.onclass.persona.domain.spi.ReporteClientPort;
import com.onclass.persona.infrastructure.adapters.utils.ClientConstants;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Component
public class ReporteClientAdapter implements ReporteClientPort {

    private final WebClient webClient;

    public ReporteClientAdapter(@Value(ClientConstants.REPORTE_SERVICE_URL_PROPERTY) String baseUrl) {
        this.webClient = WebClient.builder()
                .baseUrl(baseUrl)
                .build();
    }

    @Override
    public Mono<Void> incrementarPersonasInscritas(Long bootcampId) {
        return webClient.put()
                .uri(ClientConstants.REPORTE_BOOTCAMPS_PATH, bootcampId)
                .retrieve()
                .bodyToMono(Void.class);
    }
}

package com.onclass.persona.domain.spi;

import reactor.core.publisher.Mono;

public interface ReporteClientPort {
    Mono<Void> incrementarPersonasInscritas(Long bootcampId);
}

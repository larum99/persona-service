package com.onclass.persona.domain.spi;

import com.onclass.persona.domain.utils.BootcampSummary;
import reactor.core.publisher.Mono;

public interface BootcampClientPort {
    Mono<BootcampSummary> obtenerBootcampPorId(Long bootcampId);
}

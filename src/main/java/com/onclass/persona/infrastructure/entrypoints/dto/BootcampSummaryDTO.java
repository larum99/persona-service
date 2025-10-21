package com.onclass.persona.infrastructure.entrypoints.dto;

import java.time.LocalDate;

public record BootcampSummaryDTO(
        Long id,
        String nombre,
        String descripcion,
        LocalDate fechaLanzamiento,
        Integer duracion
) {}

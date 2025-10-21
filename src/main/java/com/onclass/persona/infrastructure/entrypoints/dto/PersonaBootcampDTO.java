package com.onclass.persona.infrastructure.entrypoints.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonaBootcampDTO {
    private Long personaId;
    private Long bootcampId;
}


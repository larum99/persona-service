package com.onclass.persona.domain.model;

import java.time.LocalDateTime;

public class PersonaBootcamp {

    private Long id;
    private Long personaId;
    private Long bootcampId;
    private LocalDateTime fechaInscripcion;

    public PersonaBootcamp(Long id, Long personaId, Long bootcampId, LocalDateTime fechaInscripcion) {
        this.id = id;
        this.personaId = personaId;
        this.bootcampId = bootcampId;
        this.fechaInscripcion = fechaInscripcion;
    }

    public Long getId() { return id; }
    public Long getPersonaId() { return personaId; }
    public Long getBootcampId() { return bootcampId; }
    public LocalDateTime getFechaInscripcion() { return fechaInscripcion; }
}

package com.onclass.persona.domain.model;

public class PersonaBootcamp {

    private Long id;
    private Long personaId;
    private Long bootcampId;

    public PersonaBootcamp(Long id, Long personaId, Long bootcampId) {
        this.id = id;
        this.personaId = personaId;
        this.bootcampId = bootcampId;
    }

    public Long getId() { return id; }
    public Long getPersonaId() { return personaId; }
    public Long getBootcampId() { return bootcampId; }
}

package com.onclass.persona.infrastructure.adapters.persistence.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

@Table(name = "bootcamp_persona")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class PersonaBootcampEntity {

    @Id
    private Long id;

    @Column("id_persona")
    private Long personaId;

    @Column("id_bootcamp")
    private Long bootcampId;
}


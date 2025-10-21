package com.onclass.persona.infrastructure.adapters.persistence.mapper;

import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaBootcampEntityMapper {

    @Mapping(source = "personaId", target = "personaId")
    @Mapping(source = "bootcampId", target = "bootcampId")
    PersonaBootcamp toModel(PersonaBootcampEntity entity);

    @Mapping(source = "personaId", target = "personaId")
    @Mapping(source = "bootcampId", target = "bootcampId")
    PersonaBootcampEntity toEntity(PersonaBootcamp model);
}

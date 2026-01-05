package com.onclass.persona.infrastructure.adapters.persistence.mapper;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaEntity;
import com.onclass.persona.infrastructure.adapters.utils.MapperConstants;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapperConstants.SPRING_COMPONENT_MODEL)
public interface PersonaEntityMapper {

    Persona toModel(PersonaEntity entity);

    PersonaEntity toEntity(Persona model);
}
package com.onclass.persona.infrastructure.adapters.persistence.mapper;

import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.infrastructure.adapters.persistence.entity.PersonaBootcampEntity;
import com.onclass.persona.infrastructure.adapters.utils.MapperConstants;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = MapperConstants.SPRING_COMPONENT_MODEL)
public interface PersonaBootcampEntityMapper {

    @Mapping(source = MapperConstants.ID_FIELD, target = MapperConstants.ID_FIELD)
    @Mapping(source = MapperConstants.PERSONA_ID_FIELD, target = MapperConstants.PERSONA_ID_FIELD)
    @Mapping(source = MapperConstants.BOOTCAMP_ID_FIELD, target = MapperConstants.BOOTCAMP_ID_FIELD)
    PersonaBootcamp toModel(PersonaBootcampEntity entity);

    @Mapping(target = MapperConstants.ID_FIELD, ignore = true)
    @Mapping(source = MapperConstants.PERSONA_ID_FIELD, target = MapperConstants.PERSONA_ID_FIELD)
    @Mapping(source = MapperConstants.BOOTCAMP_ID_FIELD, target = MapperConstants.BOOTCAMP_ID_FIELD)
    PersonaBootcampEntity toEntity(PersonaBootcamp model);
}

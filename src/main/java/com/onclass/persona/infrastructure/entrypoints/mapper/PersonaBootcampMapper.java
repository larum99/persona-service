package com.onclass.persona.infrastructure.entrypoints.mapper;

import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.infrastructure.adapters.utils.MapperConstants;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaBootcampDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = MapperConstants.SPRING_COMPONENT_MODEL)
public interface PersonaBootcampMapper {

    PersonaBootcamp toModel(PersonaBootcampDTO dto);

    PersonaBootcampDTO toDTO(PersonaBootcamp model);
}

package com.onclass.persona.infrastructure.entrypoints.mapper;

import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.infrastructure.entrypoints.dto.PersonaBootcampDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface PersonaBootcampMapper {

    @Mapping(target = "id", ignore = true)
    PersonaBootcamp toModel(PersonaBootcampDTO dto);

    PersonaBootcampDTO toDTO(PersonaBootcamp model);
}

package com.onclass.persona.application.config;

import com.onclass.persona.domain.api.PersonaBootcampServicePort;
import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.domain.usecase.PersonaBootcampUsecase;
import com.onclass.persona.infrastructure.adapters.persistence.PersonaBootcampPersistenceAdapter;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaBootcampEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaBootcampRepository;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class UseCasesConfig {

    @Bean
    public PersonaBootcampPersistencePort personaBootcampPersistencePort(
            PersonaBootcampRepository repository,
            PersonaBootcampEntityMapper mapper
    ) {
        return new PersonaBootcampPersistenceAdapter(repository, mapper);
    }

    @Bean
    public PersonaBootcampServicePort personaBootcampServicePort(
            PersonaBootcampPersistencePort persistencePort,
            BootcampClientPort bootcampClientPort
    ) {
        return new PersonaBootcampUsecase(persistencePort, bootcampClientPort);
    }
}

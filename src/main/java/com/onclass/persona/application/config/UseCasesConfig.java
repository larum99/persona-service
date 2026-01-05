package com.onclass.persona.application.config;

import com.onclass.persona.domain.api.PersonaBootcampServicePort;
import com.onclass.persona.domain.api.PersonaServicePort;
import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import com.onclass.persona.domain.spi.ReporteClientPort;
import com.onclass.persona.domain.usecase.PersonaBootcampUsecase;
import com.onclass.persona.domain.usecase.PersonaUseCase;
import com.onclass.persona.infrastructure.adapters.persistence.PersonaBootcampPersistenceAdapter;
import com.onclass.persona.infrastructure.adapters.persistence.PersonaPersistenceAdapter;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaBootcampEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.mapper.PersonaEntityMapper;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaBootcampRepository;
import com.onclass.persona.infrastructure.adapters.persistence.repository.PersonaRepository;
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
            BootcampClientPort bootcampClientPort,
            ReporteClientPort reporteClientPort) {
        return new PersonaBootcampUsecase(persistencePort, bootcampClientPort, reporteClientPort);
    }

    @Bean
    public PersonaPersistencePort personaPersistencePort(
            PersonaRepository repository,
            PersonaEntityMapper mapper
    ) {
        return new PersonaPersistenceAdapter(repository, mapper);
    }

    @Bean
    public PersonaServicePort personaServicePort(PersonaPersistencePort persistencePort) {
        return new PersonaUseCase(persistencePort);
    }
}

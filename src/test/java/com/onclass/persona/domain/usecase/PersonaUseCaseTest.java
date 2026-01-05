package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.model.Persona;
import com.onclass.persona.domain.spi.PersonaPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonaUseCaseTest {

    @Mock
    private PersonaPersistencePort persistencePort;

    private PersonaUseCase personaUseCase;

    @BeforeEach
    void setUp() {
        personaUseCase = new PersonaUseCase(persistencePort);
    }

    @Test
    void obtenerPersonaPorId_DeberiaRetornarPersona_CuandoExiste() {
        // Arrange
        Long personaId = 1L;
        Persona persona = new Persona(1L, "Juan Perez", "juan@test.com", 25);
        when(persistencePort.obtenerPersonaPorId(personaId)).thenReturn(Mono.just(persona));

        // Act
        Mono<Persona> result = personaUseCase.obtenerPersonaPorId(personaId);

        // Assert
        StepVerifier.create(result)
                .expectNext(persona)
                .verifyComplete();
    }

    @Test
    void obtenerPersonaPorId_DeberiaRetornarVacio_CuandoNoExiste() {
        // Arrange
        Long personaId = 999L;
        when(persistencePort.obtenerPersonaPorId(personaId)).thenReturn(Mono.empty());

        // Act
        Mono<Persona> result = personaUseCase.obtenerPersonaPorId(personaId);

        // Assert
        StepVerifier.create(result)
                .verifyComplete();
    }
}
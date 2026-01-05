package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.constants.DomainConstants;
import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.domain.spi.ReporteClientPort;
import com.onclass.persona.domain.utils.BootcampSummary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

import java.time.LocalDate;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PersonaBootcampUsecaseTest {

    @Mock
    private PersonaBootcampPersistencePort persistencePort;
    @Mock
    private BootcampClientPort bootcampClientPort;
    @Mock
    private ReporteClientPort reporteClientPort;

    private PersonaBootcampUsecase usecase;

    @BeforeEach
    void setUp() {
        usecase = new PersonaBootcampUsecase(persistencePort, bootcampClientPort, reporteClientPort);
    }

    @Test
    void inscribirPersonaEnBootcamps_DeberiaRetornarError_CuandoListaEsVacia() {
        // Arrange
        List<PersonaBootcamp> relaciones = List.of();
        String messageId = "test-id";

        // Act
        Flux<PersonaBootcamp> result = usecase.inscribirPersonaEnBootcamps(relaciones, messageId);

        // Assert
        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void inscribirPersonaEnBootcamps_DeberiaRetornarError_CuandoExcedeLimite() {
        // Arrange
        Long personaId = 1L;
        List<PersonaBootcamp> relaciones = List.of(new PersonaBootcamp(null, personaId, 1L));
        List<PersonaBootcamp> inscripcionesActuales = crearInscripcionesExistentes(personaId, DomainConstants.MAX_BOOTCAMPS_PER_PERSONA);
        String messageId = "test-id";

        when(persistencePort.findBootcampsByPersonaId(personaId))
                .thenReturn(Flux.fromIterable(inscripcionesActuales));

        // Act
        Flux<PersonaBootcamp> result = usecase.inscribirPersonaEnBootcamps(relaciones, messageId);

        // Assert
        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    @Test
    void inscribirPersonaEnBootcamps_DeberiaInscribir_CuandoNoHaySolapamientos() {
        // Arrange
        Long personaId = 1L;
        PersonaBootcamp relacion = new PersonaBootcamp(null, personaId, 1L);
        List<PersonaBootcamp> relaciones = List.of(relacion);
        BootcampSummary bootcamp = new BootcampSummary(1L, "Test", "Desc", LocalDate.now().plusDays(10), 4);
        String messageId = "test-id";

        when(persistencePort.findBootcampsByPersonaId(personaId)).thenReturn(Flux.empty());
        when(bootcampClientPort.obtenerBootcampPorId(1L)).thenReturn(Mono.just(bootcamp));
        when(persistencePort.savePersonaBootcamp(any())).thenReturn(Mono.just(relacion));
        when(reporteClientPort.incrementarPersonasInscritas(anyLong())).thenReturn(Mono.empty());

        // Act
        Flux<PersonaBootcamp> result = usecase.inscribirPersonaEnBootcamps(relaciones, messageId);

        // Assert
        StepVerifier.create(result)
                .expectNext(relacion)
                .verifyComplete();
    }

    @Test
    void obtenerPersonasPorBootcampId_DeberiaRetornarPersonas_CuandoBootcampExiste() {
        // Arrange
        Long bootcampId = 1L;
        BootcampSummary bootcamp = new BootcampSummary(1L, "Test", "Desc", LocalDate.now(), 4);
        PersonaBootcamp persona = new PersonaBootcamp(1L, 1L, bootcampId);
        String messageId = "test-id";

        when(bootcampClientPort.obtenerBootcampPorId(bootcampId)).thenReturn(Mono.just(bootcamp));
        when(persistencePort.findPersonasByBootcampId(bootcampId)).thenReturn(Flux.just(persona));

        // Act
        Flux<PersonaBootcamp> result = usecase.obtenerPersonasPorBootcampId(bootcampId, messageId);

        // Assert
        StepVerifier.create(result)
                .expectNext(persona)
                .verifyComplete();
    }

    @Test
    void obtenerPersonasPorBootcampId_DeberiaRetornarError_CuandoBootcampNoExiste() {
        // Arrange
        Long bootcampId = 999L;
        String messageId = "test-id";
        RuntimeException error = new RuntimeException(DomainConstants.HTTP_NOT_FOUND_CODE + " Not Found");

        when(bootcampClientPort.obtenerBootcampPorId(bootcampId)).thenReturn(Mono.error(error));

        // Act
        Flux<PersonaBootcamp> result = usecase.obtenerPersonasPorBootcampId(bootcampId, messageId);

        // Assert
        StepVerifier.create(result)
                .expectError(BusinessException.class)
                .verify();
    }

    private List<PersonaBootcamp> crearInscripcionesExistentes(Long personaId, int cantidad) {
        return java.util.stream.IntStream.range(1, cantidad + 1)
                .mapToObj(i -> new PersonaBootcamp((long) i, personaId, (long) i))
                .toList();
    }
}
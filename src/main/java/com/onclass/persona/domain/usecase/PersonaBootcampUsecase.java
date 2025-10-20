package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.api.PersonaBootcampServicePort;
import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.domain.utils.BootcampSummary;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

public class PersonaBootcampUsecase implements PersonaBootcampServicePort {

    private final PersonaBootcampPersistencePort personaBootcampPersistencePort;
    private final BootcampClientPort bootcampClientPort;

    public PersonaBootcampUsecase(PersonaBootcampPersistencePort personaBootcampPersistencePort,
                                  BootcampClientPort bootcampClientPort) {
        this.personaBootcampPersistencePort = personaBootcampPersistencePort;
        this.bootcampClientPort = bootcampClientPort;
    }

    @Override
    public Mono<Void> registrarPersonaBootcamp(Long personaId, Long bootcampId) {
        // 1️⃣ Consultar bootcamp destino
        return bootcampClientPort.obtenerBootcampPorId(bootcampId)
                .switchIfEmpty(Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_NOT_FOUND)))
                .flatMap(bootcampNuevo ->
                        // 2️⃣ Obtener inscripciones actuales
                        personaBootcampPersistencePort.findBootcampsByPersonaId(personaId)
                                .collectList()
                                .flatMap(inscripciones ->
                                        validarRestricciones(inscripciones, bootcampNuevo, bootcampId)
                                                .then(
                                                        // 3️⃣ Guardar relación si pasa validaciones
                                                        personaBootcampPersistencePort.savePersonaBootcamp(
                                                                new PersonaBootcamp(null, personaId, bootcampId, LocalDateTime.now())
                                                        ).then()
                                                )
                                )
                );
    }

    private Mono<Void> validarRestricciones(List<PersonaBootcamp> inscripciones,
                                            BootcampSummary bootcampNuevo,
                                            Long bootcampIdNuevo) {

        // Máximo 5 bootcamps
        if (inscripciones.size() >= 5) {
            return Mono.error(new BusinessException(TechnicalMessage.MAX_BOOTCAMPS_REACHED));
        }

        // Ya inscrito
        boolean yaInscrito = inscripciones.stream()
                .anyMatch(i -> i.getBootcampId().equals(bootcampIdNuevo));
        if (yaInscrito) {
            return Mono.error(new BusinessException(TechnicalMessage.ALREADY_ENROLLED));
        }

        // Si no hay inscripciones previas, pasa sin validar solapamiento
        if (inscripciones.isEmpty()) {
            return Mono.empty();
        }

        // 4️⃣ Validar solapamiento de fechas con llamadas reactivas a BootcampClientPort
        return Flux.fromIterable(inscripciones)
                .flatMap(inscripcion -> bootcampClientPort.obtenerBootcampPorId(inscripcion.getBootcampId()))
                .collectList()
                .flatMap(bootcampsInscritos -> {
                    boolean haySolapamiento = bootcampsInscritos.stream()
                            .anyMatch(b -> fechasSeSolapan(
                                    b.getFechaLanzamiento(),
                                    b.getFechaFin(),
                                    bootcampNuevo.getFechaLanzamiento(),
                                    bootcampNuevo.getFechaFin()
                            ));

                    if (haySolapamiento) {
                        return Mono.error(new BusinessException(TechnicalMessage.BOOTCAMP_OVERLAP));
                    }

                    return Mono.empty();
                });
    }

    private boolean fechasSeSolapan(LocalDate inicio1, LocalDate fin1,
                                    LocalDate inicio2, LocalDate fin2) {
        if (inicio1 == null || fin1 == null || inicio2 == null || fin2 == null) return false;
        return (inicio1.isBefore(fin2) && fin1.isAfter(inicio2));
    }
}

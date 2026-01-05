package com.onclass.persona.domain.usecase;

import com.onclass.persona.domain.api.PersonaBootcampServicePort;
import com.onclass.persona.domain.constants.DomainConstants;
import com.onclass.persona.domain.enums.TechnicalMessage;
import com.onclass.persona.domain.exceptions.BusinessException;
import com.onclass.persona.domain.model.PersonaBootcamp;
import com.onclass.persona.domain.spi.BootcampClientPort;
import com.onclass.persona.domain.spi.PersonaBootcampPersistencePort;
import com.onclass.persona.domain.spi.ReporteClientPort;
import com.onclass.persona.domain.utils.BootcampSummary;
import reactor.core.publisher.Flux;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonaBootcampUsecase implements PersonaBootcampServicePort {

    private final PersonaBootcampPersistencePort personaBootcampPersistencePort;
    private final BootcampClientPort bootcampClientPort;
    private final ReporteClientPort reporteClientPort;

    public PersonaBootcampUsecase(PersonaBootcampPersistencePort personaBootcampPersistencePort,
                                  BootcampClientPort bootcampClientPort,
                                  ReporteClientPort reporteClientPort) {
        this.personaBootcampPersistencePort = personaBootcampPersistencePort;
        this.bootcampClientPort = bootcampClientPort;
        this.reporteClientPort = reporteClientPort;
    }

    @Override
    public Flux<PersonaBootcamp> inscribirPersonaEnBootcamps(List<PersonaBootcamp> relaciones, String messageId) {
        if (relaciones == null || relaciones.isEmpty()) {
            return Flux.error(new BusinessException(TechnicalMessage.NO_BOOTCAMPS_SELECTED));
        }

        Long personaId = relaciones.get(0).getPersonaId();

        return personaBootcampPersistencePort.findBootcampsByPersonaId(personaId)
                .collectList()
                .flatMapMany(inscripcionesActuales -> validarYProcesarInscripciones(relaciones, inscripcionesActuales));
    }

    private Flux<PersonaBootcamp> validarYProcesarInscripciones(List<PersonaBootcamp> relaciones, List<PersonaBootcamp> inscripcionesActuales) {
        if (excedeLimiteBootcamps(inscripcionesActuales.size(), relaciones.size())) {
            return Flux.error(new BusinessException(TechnicalMessage.MAX_BOOTCAMPS_REACHED));
        }

        Set<Long> idsUnicos = extraerIdsUnicos(relaciones);
        
        if (tieneDuplicados(relaciones, idsUnicos) || yaEstaInscrito(inscripcionesActuales, idsUnicos)) {
            return Flux.error(new BusinessException(TechnicalMessage.ALREADY_ENROLLED));
        }

        return validarSolapamientosYGuardar(relaciones, inscripcionesActuales);
    }

    private boolean excedeLimiteBootcamps(int inscripcionesActuales, int nuevasInscripciones) {
        return inscripcionesActuales + nuevasInscripciones > DomainConstants.MAX_BOOTCAMPS_PER_PERSONA;
    }

    private Set<Long> extraerIdsUnicos(List<PersonaBootcamp> relaciones) {
        return relaciones.stream()
                .map(PersonaBootcamp::getBootcampId)
                .collect(Collectors.toSet());
    }

    private boolean tieneDuplicados(List<PersonaBootcamp> relaciones, Set<Long> idsUnicos) {
        return idsUnicos.size() < relaciones.size();
    }

    private boolean yaEstaInscrito(List<PersonaBootcamp> inscripcionesActuales, Set<Long> idsUnicos) {
        return inscripcionesActuales.stream()
                .anyMatch(actual -> idsUnicos.contains(actual.getBootcampId()));
    }

    private Flux<PersonaBootcamp> validarSolapamientosYGuardar(List<PersonaBootcamp> relaciones, List<PersonaBootcamp> inscripcionesActuales) {
        Flux<BootcampSummary> actuales = obtenerBootcampsActuales(inscripcionesActuales);
        Flux<BootcampSummary> nuevos = obtenerBootcampsNuevos(relaciones);

        return Flux.concat(actuales, nuevos)
                .collectList()
                .flatMapMany(bootcamps -> {
                    if (haySolapamientos(bootcamps)) {
                        return Flux.error(new BusinessException(TechnicalMessage.BOOTCAMP_OVERLAP));
                    }
                    return guardarYNotificar(relaciones);
                });
    }

    private Flux<BootcampSummary> obtenerBootcampsActuales(List<PersonaBootcamp> inscripcionesActuales) {
        return Flux.fromIterable(inscripcionesActuales)
                .flatMap(pb -> bootcampClientPort.obtenerBootcampPorId(pb.getBootcampId()));
    }

    private Flux<BootcampSummary> obtenerBootcampsNuevos(List<PersonaBootcamp> relaciones) {
        return Flux.fromIterable(relaciones)
                .flatMap(pb -> bootcampClientPort.obtenerBootcampPorId(pb.getBootcampId()));
    }

    private Flux<PersonaBootcamp> guardarYNotificar(List<PersonaBootcamp> relaciones) {
        return Flux.fromIterable(relaciones)
                .flatMap(relacion ->
                        personaBootcampPersistencePort.savePersonaBootcamp(relacion)
                                .then(reporteClientPort.incrementarPersonasInscritas(relacion.getBootcampId()))
                                .thenReturn(relacion)
                );
    }

    @Override
    public Flux<PersonaBootcamp> obtenerPersonasPorBootcampId(Long bootcampId, String messageId) {
        return validarBootcampExisteYObtenerPersonas(bootcampId);
    }

    private Flux<PersonaBootcamp> validarBootcampExisteYObtenerPersonas(Long bootcampId) {
        return bootcampClientPort.obtenerBootcampPorId(bootcampId)
                .flatMapMany(bootcamp -> obtenerPersonasDelBootcamp(bootcampId))
                .onErrorMap(this::mapearErrorBootcamp);
    }

    private Flux<PersonaBootcamp> obtenerPersonasDelBootcamp(Long bootcampId) {
        return personaBootcampPersistencePort.findPersonasByBootcampId(bootcampId)
                .switchIfEmpty(Flux.error(new BusinessException(TechnicalMessage.NO_PERSONAS_FOUND)));
    }

    private Throwable mapearErrorBootcamp(Throwable ex) {
        if (esErrorBootcampNoEncontrado(ex)) {
            return new BusinessException(TechnicalMessage.BOOTCAMP_NOT_FOUND);
        }
        return ex;
    }

    private boolean esErrorBootcampNoEncontrado(Throwable ex) {
        return ex.getMessage() != null && ex.getMessage().contains(DomainConstants.HTTP_NOT_FOUND_CODE);
    }

    private boolean haySolapamientos(List<BootcampSummary> bootcamps) {
        List<BootcampSummary> ordenados = bootcamps.stream()
                .filter(b -> b.getFechaLanzamiento() != null && b.getFechaFin() != null)
                .sorted(Comparator.comparing(BootcampSummary::getFechaLanzamiento))
                .toList();

        for (int i = 0; i < ordenados.size() - 1; i++) {
            BootcampSummary actual = ordenados.get(i);
            BootcampSummary siguiente = ordenados.get(i + 1);

            if (fechasSeSolapan(
                    actual.getFechaLanzamiento(),
                    actual.getFechaFin(),
                    siguiente.getFechaLanzamiento(),
                    siguiente.getFechaFin())
            ) {
                return true;
            }
        }
        return false;
    }

    private boolean fechasSeSolapan(LocalDate inicio1, LocalDate fin1,
                                    LocalDate inicio2, LocalDate fin2) {
        return (inicio1.isBefore(fin2) && fin1.isAfter(inicio2));
    }
}

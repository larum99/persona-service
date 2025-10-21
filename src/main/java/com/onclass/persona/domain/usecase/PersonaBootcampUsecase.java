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
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class PersonaBootcampUsecase implements PersonaBootcampServicePort {

    private final PersonaBootcampPersistencePort personaBootcampPersistencePort;
    private final BootcampClientPort bootcampClientPort;

    public PersonaBootcampUsecase(PersonaBootcampPersistencePort personaBootcampPersistencePort,
                                  BootcampClientPort bootcampClientPort) {
        this.personaBootcampPersistencePort = personaBootcampPersistencePort;
        this.bootcampClientPort = bootcampClientPort;
    }

    @Override
    public Flux<PersonaBootcamp> inscribirPersonaEnBootcamps(List<PersonaBootcamp> relaciones, String messageId) {
        if (relaciones == null || relaciones.isEmpty()) {
            return Flux.error(new BusinessException(TechnicalMessage.NO_BOOTCAMPS_SELECTED));
        }

        Long personaId = relaciones.get(0).getPersonaId();

        return personaBootcampPersistencePort.findBootcampsByPersonaId(personaId)
                .collectList()
                .flatMapMany(inscripcionesActuales -> {

                    // 1️⃣ Validar máximo 5 bootcamps
                    if (inscripcionesActuales.size() + relaciones.size() > 5) {
                        return Flux.error(new BusinessException(TechnicalMessage.MAX_BOOTCAMPS_REACHED));
                    }

                    // 2️⃣ Validar duplicados en la misma petición
                    Set<Long> idsUnicos = relaciones.stream()
                            .map(PersonaBootcamp::getBootcampId)
                            .collect(Collectors.toSet());

                    if (idsUnicos.size() < relaciones.size()) {
                        return Flux.error(new BusinessException(TechnicalMessage.ALREADY_ENROLLED));
                    }

                    // 3️⃣ Validar que no esté inscrito ya en alguno
                    boolean yaInscrito = inscripcionesActuales.stream()
                            .anyMatch(actual -> idsUnicos.contains(actual.getBootcampId()));

                    if (yaInscrito) {
                        return Flux.error(new BusinessException(TechnicalMessage.ALREADY_ENROLLED));
                    }

                    // 4️⃣ Obtener los bootcamps (actuales + nuevos)
                    Flux<BootcampSummary> actuales = Flux.fromIterable(inscripcionesActuales)
                            .flatMap(pb -> bootcampClientPort.obtenerBootcampPorId(pb.getBootcampId()));

                    Flux<BootcampSummary> nuevos = Flux.fromIterable(relaciones)
                            .flatMap(pb -> bootcampClientPort.obtenerBootcampPorId(pb.getBootcampId()));

                    return Flux.concat(actuales, nuevos)
                            .collectList()
                            .flatMapMany(bootcamps -> {

                                // 5️⃣ Validar solapamientos
                                if (haySolapamientos(bootcamps)) {
                                    return Flux.error(new BusinessException(TechnicalMessage.BOOTCAMP_OVERLAP));
                                }

                                // 6️⃣ Si pasa todo → guardar
                                return Flux.fromIterable(relaciones)
                                        .flatMap(personaBootcampPersistencePort::savePersonaBootcamp);
                            });
                });
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

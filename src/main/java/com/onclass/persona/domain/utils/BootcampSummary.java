package com.onclass.persona.domain.utils;

import java.time.LocalDate;

public class BootcampSummary {

    private Long id;
    private String nombre;
    private String descripcion;
    private LocalDate fechaLanzamiento;
    private Integer duracion; // en semanas

    public BootcampSummary(Long id, String nombre, String descripcion, LocalDate fechaLanzamiento, Integer duracion) {
        this.id = id;
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.fechaLanzamiento = fechaLanzamiento;
        this.duracion = duracion;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getDescripcion() { return descripcion; }
    public LocalDate getFechaLanzamiento() { return fechaLanzamiento; }
    public Integer getDuracion() { return duracion; }

    /**
     * Fecha de finalización derivada de la fecha de lanzamiento y la duración en semanas.
     */
    public LocalDate getFechaFin() {
        return fechaLanzamiento != null && duracion != null
                ? fechaLanzamiento.plusWeeks(duracion)
                : null;
    }
}

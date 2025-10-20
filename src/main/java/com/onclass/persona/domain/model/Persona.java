package com.onclass.persona.domain.model;

public class Persona {

    private Long id;
    private String nombre;
    private String correo;
    private Integer edad;

    public Persona(Long id, String nombre, String correo, Integer edad) {
        this.id = id;
        this.nombre = nombre;
        this.correo = correo;
        this.edad = edad;
    }

    public Long getId() { return id; }
    public String getNombre() { return nombre; }
    public String getCorreo() { return correo; }
    public Integer getEdad() { return edad; }
}

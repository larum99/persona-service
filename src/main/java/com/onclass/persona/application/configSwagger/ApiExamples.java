package com.onclass.persona.application.configSwagger;

public class ApiExamples {

    private ApiExamples() {}

    public static final String PERSONA_BOOTCAMP_DTO_JSON = """
        {
            "personaId": 1,
            "bootcampId": 1
        }
    """;

    // Method names
    public static final String INSCRIBIR_PERSONA_METHOD = "inscribirPersonaEnBootcamps";
    public static final String GET_PERSONAS_BY_BOOTCAMP_METHOD = "obtenerPersonasPorBootcampId";
    public static final String GET_PERSONA_BY_ID_METHOD = "obtenerPersonaPorId";
    
    // Example names
    public static final String EXAMPLE_PERSONA_BOOTCAMP_NAME = "Ejemplo Inscripción";
}
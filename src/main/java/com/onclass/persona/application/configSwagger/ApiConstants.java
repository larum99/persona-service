package com.onclass.persona.application.configSwagger;

public class ApiConstants {

    private ApiConstants() {}

    // Paths
    public static final String PATH_PERSONA_BOOTCAMP = "/persona-bootcamps";
    public static final String PATH_PERSONA = "/personas";

    // Headers
    public static final String HEADER_X_MESSAGE_ID = "x-message-id";
    public static final String HEADER_X_MESSAGE_ID_DESC = "Identificador único de la transacción para trazabilidad.";

    // Operaciones
    public static final String INSCRIBIR_PERSONA_OPERATION_ID = "inscribirPersonaEnBootcamps";
    public static final String INSCRIBIR_PERSONA_SUMMARY = "Inscribir persona en bootcamps";
    public static final String INSCRIBIR_PERSONA_DESCRIPTION = "Inscribe una persona en uno o varios bootcamps";
    
    public static final String GET_PERSONAS_BY_BOOTCAMP_OPERATION_ID = "obtenerPersonasPorBootcamp";
    public static final String GET_PERSONAS_BY_BOOTCAMP_SUMMARY = "Obtener personas por bootcamp";
    public static final String GET_PERSONAS_BY_BOOTCAMP_DESCRIPTION = "Obtiene todas las personas inscritas en un bootcamp específico";
    
    public static final String GET_PERSONA_BY_ID_OPERATION_ID = "obtenerPersonaPorId";
    public static final String GET_PERSONA_BY_ID_SUMMARY = "Obtener persona por ID";
    public static final String GET_PERSONA_BY_ID_DESCRIPTION = "Obtiene los datos de una persona específica por su ID";

    // Request body
    public static final String REQUEST_BODY_DESCRIPTION = "Datos necesarios para inscribir una persona en bootcamps.";

    // Responses
    public static final String RESPONSE_200 = "Operación exitosa.";
    public static final String RESPONSE_201 = "Inscripción realizada exitosamente.";
    public static final String RESPONSE_400 = "Solicitud inválida. Verifique los datos enviados.";
    public static final String RESPONSE_404 = "Recurso no encontrado.";
    public static final String RESPONSE_500 = "Error interno del servidor.";

    // HTTP Codes
    public static final String HTTP_OK = "200";
    public static final String HTTP_CREATED = "201";
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_NOT_FOUND = "404";
    public static final String HTTP_INTERNAL_ERROR = "500";

    // Path segments
    public static final String PATH_BOOTCAMP_ID_SEGMENT = "/bootcamp/{bootcampId}";

    // Parámetros
    public static final String PARAM_BOOTCAMP_ID = "bootcampId";
    public static final String PARAM_BOOTCAMP_ID_DESC = "ID del bootcamp";
    public static final String PARAM_PERSONA_ID = "id";
    public static final String PARAM_PERSONA_ID_DESC = "ID de la persona";
}
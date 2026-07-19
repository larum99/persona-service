package com.onclass.persona.application.configSwagger;

public final class ApiConstants {
    private ApiConstants() {}

    public static final String PATH_PERSONA_BOOTCAMP = "/persona-bootcamps";

    public static final String HEADER_X_MESSAGE_ID = "x-message-id";
    public static final String HEADER_X_MESSAGE_ID_DESC = "Identificador único del mensaje";

    public static final String INSCRIBIR_PERSONA_OPERATION_ID = "inscribirPersonaEnBootcamps";
    public static final String INSCRIBIR_PERSONA_SUMMARY = "Inscribir persona(s) en bootcamp(s)";
    public static final String INSCRIBIR_PERSONA_DESCRIPTION = "Inscribe una o más personas en uno o más bootcamps";

    public static final String REQUEST_BODY_DESCRIPTION = "Datos de inscripción persona-bootcamp";

    public static final String RESPONSE_201 = "Inscripción realizada exitosamente";
    public static final String RESPONSE_400 = "Parámetros inválidos";
    public static final String RESPONSE_500 = "Error interno";

    public static final String HTTP_CREATED = "201";
    public static final String HTTP_BAD_REQUEST = "400";
    public static final String HTTP_INTERNAL_ERROR = "500";

    public static final String SCHEMA_TYPE_STRING = "string";

    public static final String EXAMPLE_NAME_PERSONA_BOOTCAMP = "Ejemplo PersonaBootcamp";
    public static final String BEAN_METHOD_INSCRIBIR = "inscribirPersonaEnBootcamps";
}

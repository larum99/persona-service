package com.onclass.persona.domain.enums;

public enum TechnicalMessage {

    // ======== Errores genéricos ========
    INTERNAL_ERROR("500", "Ha ocurrido un error interno, por favor intente nuevamente", ""),
    INVALID_REQUEST("400", "Solicitud incorrecta, por favor verifique los datos", ""),

    // ======== Mensajes específicos de Persona-Bootcamp ========
    PERSONA_NOT_FOUND("404", "La persona no existe", "personaId"),
    BOOTCAMP_NOT_FOUND("404-1", "El bootcamp no existe", "bootcampId"),
    NO_BOOTCAMPS_SELECTED("400-4", "Debe seleccionar al menos un bootcamp para inscribirse", "bootcampIds"),

    MAX_BOOTCAMPS_REACHED("400-1", "La persona ya está inscrita en el número máximo de bootcamps permitidos (5)", "personaId"),
    ALREADY_ENROLLED("400-2", "La persona ya está inscrita en este bootcamp", "bootcampId"),
    BOOTCAMP_OVERLAP("400-3", "El bootcamp se solapa en fechas con otro en el que la persona ya está inscrita", "bootcampId"),

    // ======== Mensajes de persistencia ========
    PERSONA_BOOTCAMP_SAVE_FAILED("500-1", "No se pudo registrar la inscripción de la persona al bootcamp", ""),

    // ======== Mensaje de éxito ========
    PERSONA_BOOTCAMP_REGISTERED("201", "Inscripción de la persona al bootcamp registrada exitosamente", "");

    private final String code;
    private final String description;
    private final String param;

    TechnicalMessage(String code, String description, String param) {
        this.code = code;
        this.description = description;
        this.param = param;
    }

    public String getCode() {
        return code;
    }

    public String getDescription() {
        return description;
    }

    public String getParam() {
        return param;
    }
}

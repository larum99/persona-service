package com.onclass.persona.infrastructure.adapters.utils;

public class ClientConstants {

    private ClientConstants() {}

    // Paths
    public static final String BOOTCAMPS_PATH = "/bootcamps/{bootcampId}";
    public static final String REPORTE_BOOTCAMPS_PATH = "/reporte-bootcamps/{bootcampId}/personas";

    // Headers
    public static final String DEFAULT_MESSAGE_ID = "12345";

    // HTTP Status
    public static final int HTTP_NOT_FOUND = 404;

    // Configuration properties
    public static final String BOOTCAMP_SERVICE_URL_PROPERTY = "${services.bootcamp.url}";
    public static final String REPORTE_SERVICE_URL_PROPERTY = "${services.reporte.url}";

    // Error messages
    public static final String NOT_FOUND_ERROR = "404 Not Found";
    public static final String CLIENT_ERROR_PREFIX = "Client error: ";
}
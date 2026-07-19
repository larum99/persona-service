# Bootcamp - Persona Microservice

[![Java](https://img.shields.io/badge/Java-17-ED8B00?style=flat-square&logo=openjdk&logoColor=white)](https://www.oracle.com/java/technologies/javase/jdk17-archive-downloads.html)
[![Spring Boot](https://img.shields.io/badge/Spring%20Boot-3.5.6-6DB33F?style=flat-square&logo=springboot&logoColor=white)](https://spring.io/projects/spring-boot)
[![WebFlux](https://img.shields.io/badge/WebFlux-Reactive-6DB33F?style=flat-square&logo=spring&logoColor=white)](https://docs.spring.io/spring-framework/reference/web/webflux.html)
[![Gradle](https://img.shields.io/badge/Gradle-8.14-02303A?style=flat-square&logo=gradle&logoColor=white)](https://gradle.org/)
[![License](https://img.shields.io/badge/License-Apache%202.0-blue.svg)](https://opensource.org/licenses/Apache-2.0)

## Descripcion

Microservicio de gestion de personas y sus inscripciones a bootcamps para la plataforma **Bootcamp**. Permite inscribir personas en bootcamps, consultar personas por bootcamp y obtener personas por ID.

**Puerto:** `8092`
**Base Path:** `/persona-service`

## Stack Tecnologico

| Componente | Tecnologia |
|---|---|
| Lenguaje | Java 17 |
| Framework | Spring Boot 3.5.6 |
| Web | Spring WebFlux (Reactiva) |
| Base de datos | MySQL via R2DBC |
| Pool de conexiones | R2DBC Pool |
| Build Tool | Gradle |
| Mapeo | MapStruct 1.5.5 + Lombok |
| Documentacion API | SpringDoc OpenAPI 2.6.0 (WebFlux) |
| Resiliencia | Resilience4j (CircuitBreaker, Retry, Bulkhead) |
| Comunicacion | WebClient (bootcamp-service, reporte-service) |
| Monitoreo | Spring Boot Actuator |
| Testing | JUnit 5 + Mockito + Reactor Test |
| Coverage | JaCoCo 0.8.8 |

## Arquitectura

Arquitectura Hexagonal (Puertos y Adaptadores) con stack reactivo:

```
com.onclass.persona
├── application/
│   ├── config/                    # Configuracion (UseCases)
│   └── configSwagger/             # Configuracion OpenAPI/Swagger
├── domain/
│   ├── api/                       # Puertos entrantes (PersonaServicePort, PersonaBootcampServicePort)
│   ├── spi/                       # Puertos salientes (Persistence + Client ports)
│   ├── model/                     # Modelos de dominio (Persona, PersonaBootcamp)
│   ├── usecase/                   # Casos de uso (PersonaUseCase, PersonaBootcampUsecase)
│   ├── constants/                 # Constantes de dominio
│   ├── enums/                     # Mensajes tecnicos
│   ├── exceptions/                # Excepciones de dominio
│   └── utils/                     # BootcampSummary
└── infrastructure/
    ├── entrypoints/
    │   ├── RouterRest             # Rutas funcionales WebFlux
    │   ├── handler/               # PersonaHandlerImpl, PersonaBootcampHandlerImpl
    │   ├── dto/                   # PersonaBootcampDTO, BootcampSummaryDTO
    │   ├── mapper/                # PersonaBootcampMapper
    │   └── utils/                 # Constants, APIResponse, ErrorDTO
    └── adapters/
        ├── persistence/
        │   ├── entity/            # PersonaEntity, PersonaBootcampEntity
        │   ├── repository/        # PersonaRepository, PersonaBootcampRepository
        │   └── mapper/            # PersonaEntityMapper, PersonaBootcampEntityMapper
        ├── clients/               # WebClient adapters
        │   ├── BootcampClientAdapter
        │   └── ReporteClientAdapter
        └── utils/                 # ClientConstants, EntityConstants, MapperConstants
```

## Endpoints

Todos los endpoints requieren el header `x-message-id` para trazabilidad.

| Metodo | Ruta | Descripcion |
|---|---|---|
| `POST` | `/persona-service/persona-bootcamps` | Inscribir persona en uno o mas bootcamps |
| `GET` | `/persona-service/persona-bootcamps/bootcamp/{bootcampId}` | Obtener personas inscritas en un bootcamp |
| `GET` | `/persona-service/personas/{id}` | Obtener persona por ID |

### Request - Inscribir Persona en Bootcamps

```json
{
  "personaId": 1,
  "bootcampId": 10
}
```

**Response (201 Created):**
```json
{
  "code": "201-0",
  "message": "Persona inscrita en bootcamp exitosamente",
  "identifier": "msg-uuid-123",
  "date": "2026-07-17T10:30:00"
}
```

### Request - Obtener Personas por Bootcamp

**Response (200 OK):**
```json
{
  "code": "200-0",
  "message": "Personas obtenidas exitosamente",
  "identifier": "msg-uuid-456",
  "date": "2026-07-17T10:30:00",
  "data": [
    {
      "id": 1,
      "nombre": "Juan Perez",
      "correo": "juan@email.com",
      "edad": 25
    }
  ]
}
```

### Request - Obtener Persona por ID

**Response (200 OK):**
```json
{
  "code": "200-0",
  "message": "Persona obtenida exitosamente",
  "identifier": "msg-uuid-789",
  "date": "2026-07-17T10:30:00",
  "data": {
    "id": 1,
    "nombre": "Juan Perez",
    "correo": "juan@email.com",
    "edad": 25
  }
}
```

### Respuesta de Error

```json
{
  "code": "400-2",
  "message": "La persona ya esta inscrita en este bootcamp",
  "identifier": "msg-uuid-123",
  "date": "2026-07-17T10:30:00",
  "errors": [
    {
      "code": "400-2",
      "message": "La persona ya esta inscrita en este bootcamp",
      "param": "bootcampId"
    }
  ]
}
```

## Modelo de Datos

```
┌──────────────────┐       ┌───────────────────────┐
│     persona      │       │   bootcamp_persona    │
├──────────────────┤       ├───────────────────────┤
│ id (PK)          │◄──FK──│ id_persona (FK)       │
│ nombre           │       │ id (PK)               │
│ correo           │       │ id_bootcamp (FK)      │
│ edad             │       └───────────────────────┘
└──────────────────┘
```

## Integraciones

| Servicio | Puerto | Uso |
|---|---|---|
| bootcamp-service | 8091 | Validar existencia de bootcamp, obtener datos del bootcamp |
| reporte-service | 8093 | Notificar inscripcion de personas |

## Variables de Entorno

| Variable | Descripcion | Ejemplo |
|---|---|---|
| `DB_HOST` | Host de MySQL | `localhost` |
| `DB_PORT` | Puerto de MySQL | `3306` |
| `DB_NAME` | Nombre de la base de datos | `persona` |
| `DB_USER` | Usuario de MySQL | `root` |
| `DB_PASSWORD` | Contrasena de MySQL | `password` |

**Base de datos por defecto:** `persona`

## Resiliencia

| Patron | Nombre | Configuracion |
|---|---|---|
| CircuitBreaker | `personaDB` | Proteccion contra fallos de DB |
| Retry | `personaRetry` | Max 5 intentos, backoff exponencial |
| Bulkhead | `personaBulkhead` | Max 5 llamadas concurrentes |

## Actuator

```
/persona-service/actuator/health
/persona-service/actuator/metrics
/persona-service/actuator/loggers
```

## Ejecutar el Proyecto

```bash
cd persona-service
./gradlew bootRun
```

La aplicacion estara disponible en `http://localhost:8092`

> **Requisito:** MySQL debe estar ejecutandose en `localhost:3306` con la base de datos `persona`

## Documentacion API (Swagger)

```
http://localhost:8092/swagger-ui.html
http://localhost:8092/v3/api-docs
```

## Ejecutar Tests

```bash
./gradlew test
```

## Reglas de Negocio

- Una persona puede estar inscrita en maximo 5 bootcamps
- No se permiten inscripciones duplicadas (misma persona en mismo bootcamp)
- Se valida que no haya overlap de fechas entre bootcamps inscritos

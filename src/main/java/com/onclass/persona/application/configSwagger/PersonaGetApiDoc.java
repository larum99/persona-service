package com.onclass.persona.application.configSwagger;

import com.onclass.persona.infrastructure.entrypoints.handler.PersonaHandlerImpl;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@RouterOperation(
        path = ApiConstants.PATH_PERSONA + "/{id}",
        beanClass = PersonaHandlerImpl.class,
        beanMethod = ApiExamples.GET_PERSONA_BY_ID_METHOD,
        operation = @Operation(
                operationId = ApiConstants.GET_PERSONA_BY_ID_OPERATION_ID,
                summary = ApiConstants.GET_PERSONA_BY_ID_SUMMARY,
                description = ApiConstants.GET_PERSONA_BY_ID_DESCRIPTION,
                parameters = {
                        @Parameter(
                                name = ApiConstants.PARAM_PERSONA_ID,
                                in = ParameterIn.PATH,
                                description = ApiConstants.PARAM_PERSONA_ID_DESC,
                                required = true
                        )
                },
                responses = {
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_OK,
                                description = ApiConstants.RESPONSE_200
                        ),
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_NOT_FOUND,
                                description = ApiConstants.RESPONSE_404
                        ),
                        @ApiResponse(
                                responseCode = ApiConstants.HTTP_INTERNAL_ERROR,
                                description = ApiConstants.RESPONSE_500
                        )
                }
        )
)
public @interface PersonaGetApiDoc {}
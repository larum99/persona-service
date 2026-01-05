package com.onclass.persona.application.configSwagger;

import com.onclass.persona.infrastructure.entrypoints.handler.PersonaBootcampHandlerImpl;
import com.onclass.persona.infrastructure.entrypoints.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.enums.ParameterIn;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.springdoc.core.annotations.RouterOperation;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@Retention(RetentionPolicy.RUNTIME)
@RouterOperation(
        path = Constants.PERSONA_BOOTCAMP_PATH + ApiConstants.PATH_BOOTCAMP_ID_SEGMENT,
        beanClass = PersonaBootcampHandlerImpl.class,
        beanMethod = ApiExamples.GET_PERSONAS_BY_BOOTCAMP_METHOD,
        operation = @Operation(
                operationId = ApiConstants.GET_PERSONAS_BY_BOOTCAMP_OPERATION_ID,
                summary = ApiConstants.GET_PERSONAS_BY_BOOTCAMP_SUMMARY,
                description = ApiConstants.GET_PERSONAS_BY_BOOTCAMP_DESCRIPTION,
                parameters = {
                        @Parameter(
                                name = ApiConstants.HEADER_X_MESSAGE_ID,
                                in = ParameterIn.HEADER,
                                description = ApiConstants.HEADER_X_MESSAGE_ID_DESC,
                                required = false
                        ),
                        @Parameter(
                                name = ApiConstants.PARAM_BOOTCAMP_ID,
                                in = ParameterIn.PATH,
                                description = ApiConstants.PARAM_BOOTCAMP_ID_DESC,
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
public @interface PersonaBootcampGetApiDoc {}
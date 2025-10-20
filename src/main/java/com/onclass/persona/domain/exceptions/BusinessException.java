package com.onclass.persona.domain.exceptions;

import com.onclass.persona.domain.enums.TechnicalMessage;
import lombok.Getter;

@Getter
public class BusinessException extends ProcessorException {

    public BusinessException(TechnicalMessage technicalMessage) {
        super(technicalMessage.getDescription(), technicalMessage);
    }
}
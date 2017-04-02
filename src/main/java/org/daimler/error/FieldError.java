package org.daimler.error;

import com.fasterxml.jackson.databind.JsonNode;
import lombok.Data;

/**
 * @author abhilash.ghosh
 */
@Data
public class FieldError {

    private final String objectName;
    private final String field;
    private Object rejectedValue;
    private final String message;

    public FieldError(org.springframework.validation.FieldError error) {
        objectName = error.getObjectName();
        field = error.getField();
        message = error.getDefaultMessage();
        rejectedValue = error.getRejectedValue();
    }
}

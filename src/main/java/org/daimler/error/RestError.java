package org.daimler.error;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

import javax.validation.constraints.NotNull;

/**
 * Pogo to handle Rest related error message.
 *
 * @author abhilash.ghosh
 */
@Data
@AllArgsConstructor
@RequiredArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestError {

    @NotNull
    private final HttpStatus status;

    private final String message;

    private Object details;
}

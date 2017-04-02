package org.daimler.error;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * @author abhilash.ghosh
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class EntityAlreadyExistsException extends Exception {

    public EntityAlreadyExistsException(String message) {
        super(message);
    }
}

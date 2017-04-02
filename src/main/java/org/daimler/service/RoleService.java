package org.daimler.service;

import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.daimler.error.ResourceNotFoundException;

import java.util.Set;

/**
 * @author abhilash.ghosh
 */

public interface RoleService {

    /**
     * Retrieves the role
     *
     * @param string to look for
     * @return Role
     * @throws ResourceNotFoundException
     */
    Role get(RoleName string) throws ResourceNotFoundException;

}

package org.daimler.security.repository;

import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

/**
 * Repository to manage the user {@link Role}.
 * @author Abhilash Ghosh
 */
public interface RoleRepository extends JpaRepository<Role, Long> {

    @Query("select r from Role r where r.name = ?1")
    Role findByRoleName(RoleName roleName);
}

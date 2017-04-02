package org.daimler.repository;

import org.daimler.entity.user.Role;

import org.daimler.entity.user.RoleName;
import org.hibernate.Query;
import org.springframework.stereotype.Repository;


/**
 * DAO to manage the user {@link Role}.
 *
 * @author Abhilash Ghosh
 */
@Repository
public class RoleDAO extends AbstractDAO<Role> {

    public RoleDAO(){
        setClazz(Role.class);
    }

    public Role findByRoleName(RoleName roleName){
        Query query = getCurrentSession()
                        .createQuery( "from Role r where r.name =:roleName");
        query.setParameter("roleName", roleName);
        return (Role) query.uniqueResult();
    }
}

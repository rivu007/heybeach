package org.daimler.service.impl;

import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.daimler.error.ResourceNotFoundException;
import org.daimler.repository.RoleDAO;
import org.daimler.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleDAO roleDAO;

    @Override
    public Role get(RoleName roleName) throws ResourceNotFoundException {
        return roleDAO.findByRoleName(roleName);
    }
}

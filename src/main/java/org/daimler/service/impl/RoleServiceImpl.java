package org.daimler.service.impl;

import org.daimler.entity.user.Role;
import org.daimler.entity.user.RoleName;
import org.daimler.error.ResourceNotFoundException;
import org.daimler.security.repository.RoleRepository;
import org.daimler.service.RoleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleServiceImpl implements RoleService {

    @Autowired
    private RoleRepository roleRepository;

    @Override
    public Role get(RoleName roleName) throws ResourceNotFoundException {
        return roleRepository.findByRoleName(roleName);
    }
}

package org.example.service;

import org.example.model.Role;
import org.example.enumerate.RoleType;
import org.example.repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RoleService {

    @Autowired
    private RoleRepository roleRepository;

    public Role findByName(String name) {
        return roleRepository.findByName(name);
    }

    public Role findByType(RoleType type) {
        return roleRepository.findByType(type);
    }
}

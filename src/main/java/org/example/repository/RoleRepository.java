package org.example.repository;

import org.example.model.Role;
import org.example.enumerate.RoleType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
    Role findByName(String name);
    Role findByType(RoleType type);
}

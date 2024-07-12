package org.example.repository;

import org.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
    boolean existsByEmail(String email);
    Optional<User> findByUsername(String username);
    boolean existsByEmailAndRolesContaining(String email, String role);
    boolean existsByPhoneNumberAndRolesContaining(String phoneNumber, String role);
    User findByEmail(String email);
    Optional<User> findByPhoneNumber(String phoneNumber);
    List<User> findAllByRolesContaining(String role);
}

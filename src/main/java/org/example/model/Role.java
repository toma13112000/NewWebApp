package org.example.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import lombok.Data;
import org.example.enumerate.RoleType;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@Data
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 191)
    private String name;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private RoleType type;

    @ManyToMany(mappedBy = "roles")
    @JsonBackReference
    private Set<User> users = new HashSet<>();

    public Role() {
    }

    public Role(String name, RoleType type) {
        this.name = name;
        this.type = type;
    }

    public void setUsers(Set<User> users) {
        this.users = (users == null) ? new HashSet<>() : users;
    }

    public RoleType getType() {
        return type;
    }
}

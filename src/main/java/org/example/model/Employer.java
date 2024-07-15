package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.enumerate.RoleType;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("EMPLOYER")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Employer extends User {

    @Column(length = 191)
    private String companyName;

    @Column(length = 191)
    private String companyUrl;

    @Column(name = "company_activity", columnDefinition = "TEXT")
    private String companyActivity;

    @Enumerated(EnumType.STRING)
    @Column(name = "role_type")
    private RoleType roleType;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Advertisement> advertisements;
}

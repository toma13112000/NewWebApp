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

    @ElementCollection
    @CollectionTable(name = "company_activities", joinColumns = @JoinColumn(name = "employer_id"))
    @Column(name = "activity", columnDefinition = "TEXT")
    private List<String> companyActivities;

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private RoleType roleType;

    @OneToMany(mappedBy = "employer", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Advertisement> advertisements;
}

package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.enumerate.RoleType;

import javax.persistence.*;

@Entity
@DiscriminatorValue("RECRUITING_COMPANY")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecruitingCompany extends User {

    @Enumerated(EnumType.STRING)
    @Column(length = 50)
    private RoleType roleType;

    // Других полей нет, так как все унаследованы от User
}

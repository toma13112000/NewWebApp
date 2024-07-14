package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.enumerate.RoleType;

import javax.persistence.*;
import java.util.List;

@Entity
@DiscriminatorValue("RECRUITING_COMPANY")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecruitingCompany extends User {

    @OneToMany(mappedBy = "recruitingCompany", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Advertisement> advertisements;

    // Других полей нет, так как все унаследованы от User
}

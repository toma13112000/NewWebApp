package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import org.example.enumerate.RoleType;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@Entity
@DiscriminatorValue("RECRUITING_COMPANY")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class RecruitingCompany extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор рекрутинговой компании

    @OneToMany(mappedBy = "recruitingCompany", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Advertisement> advertisements = new ArrayList<>();

    // Других полей нет, так как все унаследованы от User
}

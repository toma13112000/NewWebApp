package org.example.model;

import lombok.Data;

import javax.persistence.*;

@Entity
@Data
public class Advertisement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String title;
    private String workSchedule; // график работы
    private String ageRange; // возраст
    private String requirements; // требования
    private String conditions; // условия
    private String description;
    private String type; // ВАКАНСИЯ или ЗАКАЗ
    private int salary;

    @ManyToOne
    @JoinColumn(name = "employer_id")
    private Employer employer;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "recruiting_company_id")
    private RecruitingCompany recruitingCompany;

    // Конструкторы
    public Advertisement() {}

    public Advertisement(String title, String workSchedule, String ageRange, String requirements, String conditions, String description, String type, int salary) {
        this.title = title;
        this.workSchedule = workSchedule;
        this.ageRange = ageRange;
        this.requirements = requirements;
        this.conditions = conditions;
        this.description = description;
        this.type = type;
        this.salary = salary;
    }
}

package org.example.model;

import javax.persistence.*;

@Entity
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

    // Геттеры и сеттеры
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWorkSchedule() {
        return workSchedule;
    }

    public void setWorkSchedule(String workSchedule) {
        this.workSchedule = workSchedule;
    }

    public String getAgeRange() {
        return ageRange;
    }

    public void setAgeRange(String ageRange) {
        this.ageRange = ageRange;
    }

    public String getRequirements() {
        return requirements;
    }

    public void setRequirements(String requirements) {
        this.requirements = requirements;
    }

    public String getConditions() {
        return conditions;
    }

    public void setConditions(String conditions) {
        this.conditions = conditions;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public int getSalary() {
        return salary;
    }

    public void setSalary(int salary) {
        this.salary = salary;
    }

    public Employer getEmployer() {
        return employer;
    }

    public void setEmployer(Employer employer) {
        this.employer = employer;
    }
}

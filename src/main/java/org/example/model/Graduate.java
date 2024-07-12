package org.example.model;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("GRADUATE")
@Data
@EqualsAndHashCode(callSuper = true)
@ToString(callSuper = true)
public class Graduate extends User {

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "rating")
    private Integer rating;

    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(nullable = true)
    private byte[] photo;

    @Column(length = 10, nullable = true)
    private String photoExtension;

    @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PortfolioFile> portfolioFiles;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "graduate_job_types", joinColumns = @JoinColumn(name = "graduate_id"))
    @Column(name = "job_type", length = 191)
    private List<String> jobTypes;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "graduate_employment_statuses", joinColumns = @JoinColumn(name = "graduate_id"))
    @Column(name = "employment_status", length = 191)
    private List<String> employmentStatuses;

    @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<CertificateRegistry> certificateRegistryList;

    // Конструкторы, геттеры и сеттеры

    public List<String> getPortfolio() {
        return portfolioFiles != null ? portfolioFiles.stream()
                .map(PortfolioFile::getFileName)
                .collect(Collectors.toList()) : null;
    }

    public void setJobTypes(List<String> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public void setEmploymentStatuses(List<String> employmentStatuses) {
        this.employmentStatuses = employmentStatuses;
    }

    public void setCertificateRegistryList(List<CertificateRegistry> certificateRegistryList) {
        this.certificateRegistryList = certificateRegistryList;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }
}

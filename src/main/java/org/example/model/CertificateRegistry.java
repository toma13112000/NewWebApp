package org.example.model;

import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Entity
@Table(name = "certificate_registry")
@Data
public class CertificateRegistry {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "graduate_id", nullable = false)
    private Graduate graduate;

    @Column(name = "certificate_number", length = 191)
    private String certificateNumber;

    @Column(name = "certificate_date")
    private LocalDate certificateDate;

    @Column(name = "specialty", length = 191)
    private String specialty;

    // Конструкторы, геттеры и сеттеры
    public CertificateRegistry() {}

    public CertificateRegistry(Graduate graduate, String certificateNumber, LocalDate certificateDate, String specialty) {
        this.graduate = graduate;
        this.certificateNumber = certificateNumber;
        this.certificateDate = certificateDate;
        this.specialty = specialty;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Graduate getGraduate() {
        return graduate;
    }

    public void setGraduate(Graduate graduate) {
        this.graduate = graduate;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public LocalDate getCertificateDate() {
        return certificateDate;
    }

    public void setCertificateDate(LocalDate certificateDate) {
        this.certificateDate = certificateDate;
    }

    public String getSpecialty() {
        return specialty;
    }

    public void setSpecialty(String specialty) {
        this.specialty = specialty;
    }
}

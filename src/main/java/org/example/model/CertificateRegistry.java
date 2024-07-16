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

    // Constructors
    public CertificateRegistry() {}

    public CertificateRegistry(Graduate graduate, String certificateNumber, LocalDate certificateDate, String specialty) {
        this.graduate = graduate;
        this.certificateNumber = certificateNumber;
        this.certificateDate = certificateDate;
        this.specialty = specialty;
    }
}
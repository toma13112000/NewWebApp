package org.example.DTO;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import java.time.LocalDate;

public class CertificateDTO {

    @NotEmpty(message = "Certificate number is required")
    private String certificateNumber;

    @Past(message = "Certificate date must be in the past")
    private LocalDate certificateDate;

    @NotEmpty(message = "Specialty is required")
    private String specialty;

    // Геттеры и сеттеры

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

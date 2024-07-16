package org.example.model;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Entity
@DiscriminatorValue("GRADUATE")
@Data
@EqualsAndHashCode(callSuper = true, exclude = "certificateRegistryList")
@ToString(callSuper = true, exclude = "certificateRegistryList")
public class Graduate extends User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // Уникальный идентификатор выпускника

    @Column(name = "birth_date")
    private LocalDate birthDate;

    @Column(name = "rating")
    private Double rating = 0.0; // Изначально рейтинг установлен на 0.0

    @Column(name = "photo_file", nullable = false)
    private String photoFile; // Поле для хранения пути к фото

    @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<PortfolioFile> portfolioFiles = new ArrayList<>(); // Инициализация списка

    @Column(name = "job_type", length = 191)
    private String jobType;

    @Column(name = "employment_status", length = 191)
    private String employmentStatus;

    @OneToMany(mappedBy = "graduate", cascade = CascadeType.ALL, orphanRemoval = true)
    @JsonManagedReference
    private List<CertificateRegistry> certificateRegistryList = new ArrayList<>();

    // Конструкторы, геттеры и сеттеры
    public String getJobType() {
        return jobType;
    }

    public void setJobType(String jobType) {
        this.jobType = jobType;
    }

    public String getEmploymentStatus() {
        return employmentStatus;
    }

    public void setEmploymentStatus(String employmentStatus) {
        this.employmentStatus = employmentStatus;
    }

    public List<CertificateRegistry> getCertificates() {
        return certificateRegistryList;
    }

    public void setCertificates(List<CertificateRegistry> certificates) {
        this.certificateRegistryList = certificates;
    }

    public void setRating(Integer rating) {
        // Проверка на допустимость оценки (между 1 и 5)
        if (rating < 1 || rating > 5) {
            throw new IllegalArgumentException("Рейтинг должен быть между 1 и 5");
        }

        // Проверяем, был ли уже ранее установлен рейтинг
        if (this.rating == 0.0) {
            // Если рейтинг еще не был установлен, просто устанавливаем его
            this.rating = (double) rating;
        } else {
            // Если рейтинг уже был установлен, вычисляем среднее значение
            this.rating = (this.rating + rating) / 2.0;
        }
    }

    public Double getRating() {
        return rating;
    }

    public void addCertificate(CertificateRegistry certificate) {
        certificate.setGraduate(this);
        certificateRegistryList.add(certificate);
    }

    public void removeCertificate(CertificateRegistry certificateToRemove) {
        Optional<CertificateRegistry> certificateOptional = Optional.ofNullable(certificateToRemove);
        certificateOptional.ifPresent(cert -> {
            if (certificateRegistryList.contains(cert)) {
                certificateRegistryList.remove(cert);
                cert.setGraduate(null);
            }
        });
    }

    public List<String> getPortfolio() {
        return portfolioFiles != null ? portfolioFiles.stream()
                .map(PortfolioFile::getFileName)
                .collect(Collectors.toList()) : null;
    }

    // Метод для обновления фото
    public void updatePhoto(String newPhotoFile) {
        this.photoFile = newPhotoFile;
    }

    // Метод для добавления файла в портфолио
    public void addToPortfolio(PortfolioFile file) {
        file.setGraduate(this); // Устанавливаем связь с текущим выпускником
        portfolioFiles.add(file); // Добавляем файл в список портфолио
    }
}

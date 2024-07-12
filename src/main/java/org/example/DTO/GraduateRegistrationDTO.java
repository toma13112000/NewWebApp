package org.example.DTO;

import org.example.enumerate.RoleType;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Past;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.List;

public class GraduateRegistrationDTO {

    @NotEmpty(message = "Name is required")
    @Pattern(regexp = "^[А-Яа-яЁё\\s]+$", message = "Name must be in Cyrillic")
    private String name;

    @NotEmpty(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    private RoleType roleType;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Phone number must start with +7 and contain 10 digits")
    private String phoneNumber;

    @NotEmpty(message = "Password is required")
    private String password;

    @Past(message = "Birth date must be in the past")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthDate;

    private MultipartFile photo;
    private List<MultipartFile> portfolio;  // Поле для передачи файлов портфолио

    private List<String> jobTypes; // "project" or "full-time"

    private List<String> employmentStatuses; // "active" or "inactive"

    private Integer rating; // Добавленное поле рейтинга

    private List<CertificateDTO> certificates; // Поле для списка сертификатов

    // Геттеры и сеттеры

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public MultipartFile getPhoto() {
        return photo;
    }

    public void setPhoto(MultipartFile photo) {
        this.photo = photo;
    }

    public List<MultipartFile> getPortfolio() {
        return portfolio;
    }

    public void setPortfolio(List<MultipartFile> portfolio) {
        this.portfolio = portfolio;
    }

    public List<String> getJobTypes() {
        return jobTypes;
    }

    public void setJobTypes(List<String> jobTypes) {
        this.jobTypes = jobTypes;
    }

    public List<String> getEmploymentStatuses() {
        return employmentStatuses;
    }

    public void setEmploymentStatuses(List<String> employmentStatuses) {
        this.employmentStatuses = employmentStatuses;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Integer getRating() {
        return rating;
    }

    public void setRating(Integer rating) {
        this.rating = rating;
    }

    public List<CertificateDTO> getCertificates() {
        return certificates;
    }

    public void setCertificates(List<CertificateDTO> certificates) {
        this.certificates = certificates;
    }
}

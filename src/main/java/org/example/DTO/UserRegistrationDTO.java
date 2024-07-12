package org.example.DTO;

import org.example.enumerate.RoleType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

public class UserRegistrationDTO {
    @NotEmpty(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Role is required")
    private RoleType role; // Using RoleType instead of String

    @NotEmpty(message = "Phone number is required")
    private String phoneNumber;

    private String companyName;
    private String companyUrl;
    private List<String> companyActivities;
    private LocalDate birthDate;
    private List<String> specialties;
    private String certificateNumber;
    private LocalDate certificateDate;
    private MultipartFile photo;
    private List<MultipartFile> portfolio;
    // Getters and setters
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public RoleType getRole() {
        return role;
    }

    public void setRole(RoleType role) {
        this.role = role;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getCompanyUrl() {
        return companyUrl;
    }

    public void setCompanyUrl(String companyUrl) {
        this.companyUrl = companyUrl;
    }

    public List<String> getCompanyActivities() {
        return companyActivities;
    }

    public void setCompanyActivities(List<String> companyActivities) {
        this.companyActivities = companyActivities;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(LocalDate birthDate) {
        this.birthDate = birthDate;
    }

    public List<String> getSpecialties() {
        return specialties;
    }

    public void setSpecialties(List<String> specialties) {
        this.specialties = specialties;
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
}

package org.example.DTO;

import lombok.Data;
import org.example.enumerate.RoleType;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import java.time.LocalDate;
import java.util.List;

@Data
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
}

package org.example.DTO;

import org.example.enumerate.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

public class EmployerRegistrationDTO {

    @NotEmpty(message = "Username is required")
    private String username;

    @Email(message = "Invalid email format")
    @NotEmpty(message = "Email is required")
    private String email;

    @NotEmpty(message = "Phone number is required")
    @Pattern(regexp = "^\\+7[0-9]{10}$", message = "Phone number must start with +7 and contain 10 digits")
    private String phoneNumber;

    @NotEmpty(message = "Password is required")
    private String password;

    @NotEmpty(message = "Role type is required")
    private RoleType roleType;

    private String companyName;
    private String companyUrl;
    private List<String> companyActivities;

    // Getters and setters
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


    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public RoleType getRoleType() {
        return roleType;
    }

    public void setRoleType(RoleType roleType) {
        this.roleType = roleType;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}

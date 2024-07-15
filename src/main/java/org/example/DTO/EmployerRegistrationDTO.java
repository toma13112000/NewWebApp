package org.example.DTO;

import org.example.enumerate.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.util.List;

public class EmployerRegistrationDTO {

    @NotEmpty(message = "Имя пользователя обязательно")
    private String username;

    @Email(message = "Неверный формат электронной почты")
    @NotEmpty(message = "Электронная почта обязательна")
    private String email;

    @NotEmpty(message = "Номер телефона обязателен")
    @Pattern(regexp = "^\\+7\\s?\\(\\d{3}\\)\\s?\\d{3}-\\d{2}-\\d{2}$", message = "Неверный формат номера телефона")
    private String phoneNumber;

    @NotEmpty(message = "Пароль обязателен")
    private String password;

    @NotEmpty(message = "Повторите пароль")
    private String password2;

    private RoleType roleType = RoleType.EMPLOYER;

    private String companyName;
    private String companyUrl;
    private String companyActivity;

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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPassword2() {
        return password2;
    }

    public void setPassword2(String password2) {
        this.password2 = password2;
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

    public String getCompanyActivity() {
        return companyActivity;
    }

    public void setCompanyActivity(String companyActivity) {
        this.companyActivity = companyActivity;
    }
    }

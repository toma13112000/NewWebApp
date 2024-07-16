package org.example.DTO;

import lombok.Data;
import org.example.enumerate.RoleType;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;

@Data
public class RecruitingCompanyRegistrationDTO {

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

    private RoleType roleType = RoleType.RECRUITING_COMPANY;
}

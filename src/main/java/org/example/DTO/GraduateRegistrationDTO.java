package org.example.DTO;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Pattern;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Data
public class GraduateRegistrationDTO {

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

    private LocalDate birthDate;

    private List<CertificateDTO> certificates = new ArrayList<>();

    private List<MultipartFile> portfolioFiles = new ArrayList<>();

    private MultipartFile photoFile;

    private String employmentStatus;
    private String jobType;

    public void addCertificate(CertificateDTO certificate) {
        certificates.add(certificate);
    }

    public void removeCertificate(int index) {
        if (index >= 0 && index < certificates.size()) {
            certificates.remove(index);
        }
    }
}

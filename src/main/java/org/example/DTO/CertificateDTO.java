package org.example.DTO;

import lombok.Data;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;

@Data
public class CertificateDTO {

    @NotNull(message = "Дата сертификата обязательна")
    private LocalDate certificateDate;

    @NotEmpty(message = "Номер сертификата обязателен")
    private String certificateNumber;

    @NotEmpty(message = "Специальность обязательна")
    private String specialty;
}

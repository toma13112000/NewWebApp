package org.example.DTO;

import lombok.Data;

@Data
public class ChangePasswordDTO {
    private String phoneNumber;
    private String oldPassword;
    private String newPassword;
}

package org.example.exception;

public class PhoneNumberAlreadyExistsForRoleException extends RuntimeException {
    public PhoneNumberAlreadyExistsForRoleException(String message) {
        super(message);
    }
}

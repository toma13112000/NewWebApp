package org.example.exception;

public class EmailAlreadyExistsForRoleException extends RuntimeException {
    public EmailAlreadyExistsForRoleException(String message) {
        super(message);
    }
}

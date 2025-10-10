package org.matvey.freelancebackend.users.exception;

public class UserAlreadyHasRoleException extends RuntimeException {
    public UserAlreadyHasRoleException(String message) {
        super(message);
    }
}

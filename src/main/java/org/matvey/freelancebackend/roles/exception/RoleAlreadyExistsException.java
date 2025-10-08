package org.matvey.freelancebackend.roles.exception;

public class RoleAlreadyExistsException extends RuntimeException {
    public RoleAlreadyExistsException(String field, String value) {
        super("Role with " + field + "=" + value + " already exists");
    }
}

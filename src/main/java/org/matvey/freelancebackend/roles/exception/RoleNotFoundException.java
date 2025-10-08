package org.matvey.freelancebackend.roles.exception;

public class RoleNotFoundException extends RuntimeException {
    public RoleNotFoundException(String field, String value) {
        super("Role with " + field + "=" + value + " already exists");
    }
}

package org.matvey.freelancebackend.users.exception;

public class UserDoesntHasRoleException extends RuntimeException {
  public UserDoesntHasRoleException(String message) {
    super(message);
  }
}

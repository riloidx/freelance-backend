package org.matvey.freelancebackend.common.exception;

public class IdMismatchException extends RuntimeException {
    public IdMismatchException(String entityName, long pathId, long dtoId) {
        super(entityName + " path id=" + pathId + " doesn't match DTO id=" + dtoId);
    }
}

package org.matvey.freelancebackend.category.exception;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException(String field, String value) {
        super("Category with " + field + "=" + value + " already exists");
    }
}

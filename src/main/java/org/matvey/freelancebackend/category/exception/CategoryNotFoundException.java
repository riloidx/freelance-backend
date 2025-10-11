package org.matvey.freelancebackend.category.exception;

public class CategoryNotFoundException extends RuntimeException {
    public CategoryNotFoundException(String field, String value) {
        super("Category with " + field + "=" + value + " not found");
    }
}

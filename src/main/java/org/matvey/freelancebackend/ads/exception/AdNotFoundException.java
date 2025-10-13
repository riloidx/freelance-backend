package org.matvey.freelancebackend.ads.exception;

public class AdNotFoundException extends RuntimeException {
    public  AdNotFoundException(String field, String value) {
        super("Ad with " + field + "=" + value + " not found");
    }
}

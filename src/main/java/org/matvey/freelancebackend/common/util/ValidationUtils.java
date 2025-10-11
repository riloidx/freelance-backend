package org.matvey.freelancebackend.common.util;

import org.matvey.freelancebackend.common.exception.IdMismatchException;

public final class ValidationUtils {
    public static void ensureSameId(String entityName, long pathId, long dtoId) {
        if (pathId != dtoId) {
            throw new IdMismatchException(entityName, pathId, dtoId);
        }
    }
}

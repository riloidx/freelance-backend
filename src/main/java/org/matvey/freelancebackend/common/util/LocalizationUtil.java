package org.matvey.freelancebackend.common.util;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.stereotype.Component;

import java.util.Locale;

/**
 * Utility class for handling internationalization and localization.
 * 
 * Provides convenient methods for retrieving localized messages based on
 * the current user's locale. Supports parameterized messages and uses
 * Spring's MessageSource for message resolution.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Slf4j
@Component
@RequiredArgsConstructor
public class LocalizationUtil {
    private final MessageSource messageSource;

    /**
     * Retrieves a localized message for the given key and arguments.
     * 
     * @param key the message key to look up
     * @param args optional arguments for parameterized messages
     * @return the localized message string
     */
    public String getMessage(String key, Object... args) {
        Locale currentLocale = LocaleContextHolder.getLocale();
        log.debug("Getting message for key '{}' with locale '{}'", key, currentLocale);
        
        String message = messageSource.getMessage(key, args, currentLocale);
        log.debug("Resolved message: '{}'", message);
        
        return message;
    }
}

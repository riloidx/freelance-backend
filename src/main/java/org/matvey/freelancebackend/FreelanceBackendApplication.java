package org.matvey.freelancebackend;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main application class for the Freelance Backend service.
 * 
 * This Spring Boot application provides a REST API for a freelance marketplace platform
 * that allows users to create job advertisements, submit proposals, manage contracts,
 * and handle user authentication and authorization.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@SpringBootApplication
public class FreelanceBackendApplication {

    /**
     * Main method to start the Spring Boot application.
     * 
     * @param args command line arguments passed to the application
     */
    public static void main(String[] args) {
        SpringApplication.run(FreelanceBackendApplication.class, args);
    }

}

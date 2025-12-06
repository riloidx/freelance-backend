package org.matvey.freelancebackend.roles.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

/**
 * Entity representing user roles in the freelance platform.
 * 
 * Roles define user permissions and access levels within the system.
 * Common roles include USER and ADMIN, which control access to different
 * parts of the application and API endpoints.
 * 
 * @author Matvey
 * @version 1.0
 * @since 1.0
 */
@Getter
@Setter
@Entity
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private Long id;

    @Size(max = 32)
    @NotNull
    @Column(name = "name", nullable = false)
    private String name;
}
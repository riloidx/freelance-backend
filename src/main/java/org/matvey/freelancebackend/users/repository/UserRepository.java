package org.matvey.freelancebackend.users.repository;

import org.matvey.freelancebackend.users.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAll(Pageable pageable);

    Optional<User> findByUsername(String username);

    boolean existsByUsername(String username);

    Optional<User> findByEmail(String email);

    boolean existsByEmail(String email);
}

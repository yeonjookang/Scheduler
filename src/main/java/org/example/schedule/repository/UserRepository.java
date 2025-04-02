package org.example.schedule.repository;

import jakarta.validation.constraints.NotNull;
import org.example.schedule.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;


public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(@NotNull String email);

    boolean existsByEmail(String email);
}

package ru.otus.hw.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.models.LibraryUser;

import java.util.Optional;

public interface UserRepository extends JpaRepository<LibraryUser, Long> {

    Optional<LibraryUser> findByUsername(String username);
}

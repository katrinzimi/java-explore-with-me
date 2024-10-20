package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.model.User;

public interface UserRepository extends JpaRepository<User, Long> {
}

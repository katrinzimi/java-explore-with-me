package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.model.Category;

public interface CategoriesRepository extends JpaRepository<Category, Long> {
}

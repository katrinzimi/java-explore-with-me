package ru.practicum.explorewithme.server.repository;

import org.springframework.data.jpa.repository.Query;
import ru.practicum.explorewithme.server.dto.category.CategoryDto;
import ru.practicum.explorewithme.server.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface CategoriesRepository extends JpaRepository<Category, Long>{
}

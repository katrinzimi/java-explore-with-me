package ru.practicum.explorewithme.server.repository;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.explorewithme.server.model.Compilation;

import java.util.List;

public interface CompilationRepository extends JpaRepository<Compilation,Long> {
    List<Compilation> findAllByPinned(Boolean pinned, PageRequest request);
}

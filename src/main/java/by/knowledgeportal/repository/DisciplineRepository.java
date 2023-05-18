package by.knowledgeportal.repository;

import by.knowledgeportal.entity.Discipline;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface DisciplineRepository extends JpaRepository<Discipline, Long> {

    Discipline findByName(String name);
}

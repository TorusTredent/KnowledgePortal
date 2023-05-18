package by.knowledgeportal.repository;

import by.knowledgeportal.controller.RecordController;
import by.knowledgeportal.entity.Record;
import by.knowledgeportal.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RecordRepository extends JpaRepository<Record, Long> {

    List<Record> findAllByAuthor(User user);
}

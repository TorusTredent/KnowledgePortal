package by.knowledgeportal.service;

import by.knowledgeportal.entity.Discipline;

import java.util.List;

public interface DisciplineService {
    Discipline findByName(String name);

    List<String> findAllNames();

    List<Discipline> findAll();
}

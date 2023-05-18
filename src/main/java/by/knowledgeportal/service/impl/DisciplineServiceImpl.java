package by.knowledgeportal.service.impl;

import by.knowledgeportal.entity.Discipline;
import by.knowledgeportal.repository.DisciplineRepository;
import by.knowledgeportal.service.DisciplineService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DisciplineServiceImpl implements DisciplineService {

    private final DisciplineRepository disciplineRepository;

    @Override
    public Discipline findByName(String name) {
        return disciplineRepository.findByName(name);
    }

    @Override
    public List<String> findAllNames() {
        return findAll().stream()
                .map(Discipline::getName)
                .toList();
    }

    @Override
    public List<Discipline> findAll() {
        return disciplineRepository.findAll();
    }
}

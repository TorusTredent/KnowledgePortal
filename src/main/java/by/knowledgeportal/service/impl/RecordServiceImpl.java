package by.knowledgeportal.service.impl;

import by.knowledgeportal.dto.RecordDTO;
import by.knowledgeportal.dto.RecordSaveDto;
import by.knowledgeportal.entity.Discipline;
import by.knowledgeportal.entity.Record;
import by.knowledgeportal.entity.User;
import by.knowledgeportal.entity.enums.RecordStatus;
import by.knowledgeportal.entity.enums.RecordType;
import by.knowledgeportal.exception.RecordNotFoundException;
import by.knowledgeportal.repository.RecordRepository;
import by.knowledgeportal.service.DisciplineService;
import by.knowledgeportal.service.RecordService;
import by.knowledgeportal.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class RecordServiceImpl implements RecordService {

    private final RecordRepository recordRepository;
    private final DisciplineService disciplineService;
    private final UserService userService;

    @Value("${UPLOAD_PATH}")
    private String UPLOAD_PATH;

    @Override
    public boolean upload(RecordSaveDto recordSaveDto, User user) {
        try {
            Discipline discipline = disciplineService.findByName(recordSaveDto.getDisciplineName());
            Record record = createRecord(recordSaveDto, user, discipline);

            recordSaveDto.getFile().transferTo(new File(record.getPath()));
            recordRepository.save(record);
            return true;
        } catch (IOException e) {
            log.warn(e.getMessage());
            return false;
        }
    }

    @Override
    public Resource downloadById(Long id) throws RecordNotFoundException {
        Record record = recordRepository.findById(id)
                .orElseThrow(() -> new RecordNotFoundException("Record not found"));
        return new FileSystemResource(record.getPath());
    }

    @Override
    public List<String> getAllRecordTypesNames() {
        return RecordType.getAll().stream()
                .map(RecordType::getRussian)
                .toList();
    }

    @Override
    public List<Record> getAllRecordsByAuthor(User user) {
        return recordRepository.findAllByAuthor(user);
    }

    @Override
    public List<RecordType> getAllRecordsTypes() {
        return List.of(RecordType.values());
    }

    @Override
    public List<Record> getAll() {
        return recordRepository.findAll();
    }

    @Override
    public List<RecordDTO> getAllConfirmedRecordsDto() {
        return getAll().stream()
                .filter(record -> record.getRecordStatus() == RecordStatus.CONFIRMED)
                .map(this::mapRecordToRecordDto)
                .toList();
    }


    private RecordDTO mapRecordToRecordDto(Record record) {
        return RecordDTO.builder()
                .id(record.getId())
                .name(record.getName())
                .discipline(record.getDiscipline().getName())
                .date(record.getCreate().toLocalDate())
                .build();
    }

    private String getFilePath(RecordSaveDto recordSaveDto) {
        RecordType recordType = RecordType.byText(recordSaveDto.getRecordType());
        return recordType.getPath() + recordSaveDto.getFileName() + "__" + recordSaveDto.getFile().getOriginalFilename();
    }

    private Record createRecord(RecordSaveDto recordSaveDto, User user, Discipline discipline) {
        recordSaveDto.setFileName(recordSaveDto.getFileName() + "__" + UUID.randomUUID());
        return Record.builder()
                .name(recordSaveDto.getFileName())
                .create(LocalDateTime.now())
                .discipline(discipline)
                .recordStatus(RecordStatus.PENDING)
                .recordType(RecordType.byText(recordSaveDto.getRecordType()))
                .path(getFilePath(recordSaveDto))
                .author(user)
                .build();
    }
}

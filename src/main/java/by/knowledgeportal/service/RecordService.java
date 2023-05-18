package by.knowledgeportal.service;

import by.knowledgeportal.dto.RecordDTO;
import by.knowledgeportal.dto.RecordSaveDto;
import by.knowledgeportal.entity.Record;
import by.knowledgeportal.entity.User;
import by.knowledgeportal.entity.enums.RecordType;
import by.knowledgeportal.exception.RecordNotFoundException;
import org.springframework.core.io.Resource;

import java.util.List;

public interface RecordService {
    boolean upload(RecordSaveDto recordSaveDto, User user);

    Resource downloadById(Long id) throws RecordNotFoundException;

    List<String> getAllRecordTypesNames();

    List<Record> getAllRecordsByAuthor(User user);

    List<RecordType> getAllRecordsTypes();

    List<Record> getAll();

    List<RecordDTO> getAllConfirmedRecordsDto();
}

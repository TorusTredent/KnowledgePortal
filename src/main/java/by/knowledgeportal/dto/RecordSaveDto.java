package by.knowledgeportal.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

@Data
public class RecordSaveDto {

    String disciplineName;
    MultipartFile file;
    String fileName;
    String recordType;
}

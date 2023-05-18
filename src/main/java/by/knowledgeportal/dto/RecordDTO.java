package by.knowledgeportal.dto;

import lombok.Data;
import lombok.experimental.SuperBuilder;

import java.time.LocalDate;

@Data
@SuperBuilder
public class RecordDTO {

    Long id;
    String name;
    String discipline;
    LocalDate date;
}

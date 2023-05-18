package by.knowledgeportal.entity;

import by.knowledgeportal.entity.enums.RecordStatus;
import by.knowledgeportal.entity.enums.RecordType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
@NoArgsConstructor
@SuperBuilder
@Entity
public class Record {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;
    private String path;

    private LocalDateTime create;

    @Enumerated(EnumType.STRING)
    private RecordType recordType;

    @Enumerated(EnumType.STRING)
    private RecordStatus recordStatus;

    @ManyToOne(fetch = FetchType.LAZY)
    private Discipline discipline;

    @ManyToOne(fetch = FetchType.LAZY)
    @CreatedBy
    private User author;
}

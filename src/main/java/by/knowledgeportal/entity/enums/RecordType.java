package by.knowledgeportal.entity.enums;

import by.knowledgeportal.entity.enums.constants.Paths;
import by.knowledgeportal.entity.enums.constants.RussianNaming;
import by.knowledgeportal.exception.EnumNotFoundException;

import java.util.Arrays;
import java.util.List;
import java.util.Locale;

public enum RecordType implements Paths, RussianNaming {

    LECTURE(LECTURE_PATH, RUS_LECTURE),
    PRACTICAL(PRACTICAL_PATH, RUS_PRACTICAL),
    BOOK(BOOK_PATH, RUS_BOOK);

    private final String path;
    private final String russian;

    RecordType(String path, String russian) {
        this.path = path;
        this.russian = russian;
    }

    public String getPath() {
        return path;
    }

    public String getRussian() {
        return russian;
    }

    public static List<RecordType> getAll() {
        return List.of(LECTURE, PRACTICAL, BOOK);
    }


    public static RecordType byText(String text) throws EnumNotFoundException {
        try {
            return RecordType.valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new EnumNotFoundException(String.format("Enum by text %s not found", text));
        }
    }

    public static RecordType rusByText(String text) throws EnumNotFoundException {
        return Arrays.stream(RecordType.values())
                .filter(recordType -> recordType.getRussian().equals(text))
                .findFirst()
                .orElseThrow(() -> new EnumNotFoundException(String.format("Enum by text %s not found", text)));
    }
}

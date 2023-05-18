package by.knowledgeportal.entity.enums;

import by.knowledgeportal.exception.EnumNotFoundException;

import java.util.Locale;

public enum RecordStatus {

    PENDING,
    CONFIRMED,
    REJECT;

    public static RecordStatus byText(String text) throws EnumNotFoundException {
        try {
            return RecordStatus.valueOf(text.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException e) {
            throw new EnumNotFoundException(String.format("Enum by text %s not found", text));
        }
    }
}

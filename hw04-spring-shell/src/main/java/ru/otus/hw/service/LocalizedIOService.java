package ru.otus.hw.service;

public interface LocalizedIOService extends LocalizedMessagesService, IOService {
    void printLineLocalized(String code);

    void printFormattedLineLocalized(String code, Object ...args);

    String readStringWithPromptLocalized(String promptCode);

    int readIntForRangeWithPromptAndErrorMessageLocalized(int min, int max, String prompt, String errorMessageCode);
}

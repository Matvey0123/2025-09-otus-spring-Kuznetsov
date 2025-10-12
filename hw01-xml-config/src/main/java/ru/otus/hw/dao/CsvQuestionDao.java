package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {
    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questionsFromCsv;
        try {
            var csvFileName = fileNameProvider.getTestFileName();
            var csvReader = CsvToBeanUtil.buildCsvToBean(csvFileName);
            questionsFromCsv = csvReader.parse();
        } catch (Exception e) {
            throw new QuestionReadException("CSV paring error", e);
        }

        return questionsFromCsv.stream().map(QuestionDto::toDomainObject).toList();
    }
}

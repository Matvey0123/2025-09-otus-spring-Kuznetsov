package ru.otus.hw.dao;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.dao.dto.QuestionDto;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

@Repository
@RequiredArgsConstructor
public class CsvQuestionDao implements QuestionDao {

    private static final String CSV_PARSING_ERROR_MESSAGE = "CSV paring error";

    private final TestFileNameProvider fileNameProvider;

    @Override
    public List<Question> findAll() {
        List<QuestionDto> questionsFromCsv;
        try {
            var csvFileName = fileNameProvider.getTestFileName();
            var csvReader = CsvToBeanUtil.buildCsvToBean(csvFileName);
            questionsFromCsv = csvReader.parse();
        } catch (Exception e) {
            throw new QuestionReadException(CSV_PARSING_ERROR_MESSAGE, e);
        }

        return questionsFromCsv.stream().map(QuestionDto::toDomainObject).toList();
    }
}

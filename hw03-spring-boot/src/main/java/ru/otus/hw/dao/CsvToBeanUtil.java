package ru.otus.hw.dao;

import com.opencsv.bean.CsvToBean;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.otus.hw.dao.dto.QuestionDto;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
final class CsvToBeanUtil {

    private static final int LINES_TO_SKIP = 1;

    private static final char SEPARATOR = ';';

    static CsvToBean<QuestionDto> buildCsvToBean(String fileName) throws FileNotFoundException {
        var inputStream = CsvToBeanUtil.class.getClassLoader().getResourceAsStream(fileName);
        if (inputStream == null) {
            throw new FileNotFoundException(String.format("File \"%s\" not found", fileName));
        }
        return new com.opencsv.bean.CsvToBeanBuilder<QuestionDto>(new InputStreamReader(inputStream))
                .withType(QuestionDto.class)
                .withSeparator(SEPARATOR)
                .withSkipLines(LINES_TO_SKIP)
                .build();
    }
}

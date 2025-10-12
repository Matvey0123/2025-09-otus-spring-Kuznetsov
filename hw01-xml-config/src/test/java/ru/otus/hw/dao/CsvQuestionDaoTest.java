package ru.otus.hw.dao;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.config.TestFileNameProvider;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

class CsvQuestionDaoTest {

    @InjectMocks
    private CsvQuestionDao csvQuestionDao;

    @Mock
    private TestFileNameProvider fileNameProvider;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should get questions from csv")
    @Test
    void shouldGetQuestionsFromCsv() {
        when(fileNameProvider.getTestFileName()).thenReturn("test-questions.csv");

        var questions = csvQuestionDao.findAll();

        assertThat(questions).containsExactly(
                new Question("q1?", List.of(
                        new Answer("a1", true),
                        new Answer("a2", false),
                        new Answer("a3", false))),
                new Question("q2?", List.of(
                        new Answer("a4", true),
                        new Answer("a5", false),
                        new Answer("a6", false)))
        );
    }

    @DisplayName("Should QuestionReadException upon non-existent file")
    @Test
    void shouldQuestionReadExceptionUponNoSuchFile() {
        when(fileNameProvider.getTestFileName()).thenReturn("non-existent.csv");

        var throwable = catchThrowable(() -> csvQuestionDao.findAll());

        assertThat(throwable).isInstanceOf(QuestionReadException.class);
        assertThat(throwable.getMessage()).isEqualTo("CSV paring error");
    }

}
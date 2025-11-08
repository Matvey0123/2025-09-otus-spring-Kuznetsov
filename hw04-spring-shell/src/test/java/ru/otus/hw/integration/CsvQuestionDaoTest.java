package ru.otus.hw.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.config.AppProperties;
import ru.otus.hw.dao.CsvQuestionDao;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.when;

@SpringBootTest(classes = CsvQuestionDao.class)
class CsvQuestionDaoTest {

    @MockitoBean
    private AppProperties appProperties;

    @Autowired
    private QuestionDao questionDao;

    @DisplayName("Should get questions from csv")
    @Test
    void shouldGetQuestionsFromCsv() {
        when(appProperties.getTestFileName()).thenReturn("test-questions.csv");

        var questions = questionDao.findAll();

        assertThat(questions).containsAll(getCorrectQuestions());
    }

    @DisplayName("Should QuestionReadException upon non-existent file")
    @Test
    void shouldQuestionReadExceptionUponNoSuchFile() {
        when(appProperties.getTestFileName()).thenReturn("non-existent.csv");

        var throwable = catchThrowable(() -> questionDao.findAll());

        assertThat(throwable).isInstanceOf(QuestionReadException.class);
        assertThat(throwable.getMessage()).isEqualTo("CSV paring error");
    }

    private List<Question> getCorrectQuestions() {
        var firstQuestion = new Question("q1?", List.of(
                new Answer("a1", true),
                new Answer("a2", false),
                new Answer("a3", false)));
        var secondQuestion = new Question("q2?", List.of(
                new Answer("a4", true),
                new Answer("a5", false),
                new Answer("a6", false)));
        return List.of(firstQuestion, secondQuestion);
    }

}
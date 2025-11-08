package ru.otus.hw.integration;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;
import ru.otus.hw.service.LocalizedIOService;
import ru.otus.hw.service.QuestionConverter;
import ru.otus.hw.service.TestService;
import ru.otus.hw.service.TestServiceImpl;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@SpringBootTest(classes = TestServiceImpl.class)
class TestServiceImplTest {

    @Autowired
    private TestService testService;

    @MockitoBean
    private LocalizedIOService ioService;

    @MockitoBean
    private QuestionDao questionDao;

    @MockitoBean
    private QuestionConverter questionConverter;

    @DisplayName("Should get test result upon no errors")
    @Test
    void shouldGetTestResultUponNoErrors() {
        var student = new Student("Ivan", "Ivanov");
        var questions = getCorrectQuestions();
        when(ioService.readIntForRangeWithPromptAndErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);
        when(questionDao.findAll()).thenReturn(questions);
        when(questionConverter.convertQuestionToString(any())).thenCallRealMethod();

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).containsAll(questions);
        assertThat(result.getRightAnswersCount()).isEqualTo(2);
    }

    @DisplayName("Should print error message upon QuestionReadException")
    @Test
    void shouldPrintErrorMessageUponQuestionReadException() {
        var student = new Student("Ivan", "Ivanov");
        when(questionDao.findAll()).thenThrow(QuestionReadException.class);

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).isEmpty();
        assertThat(result.getRightAnswersCount()).isEqualTo(0);
    }

    @DisplayName("Should get wrong answer exception during get answer")
    @Test
    void shouldGetWrongAnswerUponExceptionDuringGetAnswer() {
        var student = new Student("Ivan", "Ivanov");
        var questions = getCorrectQuestions();
        when(ioService.readIntForRangeWithPromptAndErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(IllegalArgumentException.class);
        when(questionDao.findAll()).thenReturn(questions);

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).containsAll(questions);
        assertThat(result.getRightAnswersCount()).isEqualTo(0);
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
package ru.otus.hw.service;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private LocalizedIOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionConverter questionConverter;

    @DisplayName("Should get test result upon no errors")
    @Test
    void shouldGetTestResultUponNoErrors() {
        var student = new Student("Ivan", "Ivanov");
        var firstQuestion = new Question("question1", List.of(
                new Answer("q1 answer1", true),
                new Answer("q1 answer2", false)));
        var secondQuestion = new Question("question2", List.of(
                new Answer("q2 answer1", false),
                new Answer("q2 answer2", true)));
        var question1AsString = "question1AsString";
        var question2AsString = "question2AsString";
        var questions = List.of(firstQuestion, secondQuestion);
        when(questionDao.findAll()).thenReturn(questions);
        when(questionConverter.convertQuestionToString(firstQuestion)).thenReturn(question1AsString);
        when(questionConverter.convertQuestionToString(secondQuestion)).thenReturn(question2AsString);
        when(ioService.readIntForRangeWithPromptAndErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString())).thenReturn(1);

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).containsAll(questions);
        assertThat(result.getRightAnswersCount()).isEqualTo(1);

        var inOrder = inOrder(questionDao, questionConverter, ioService);
        inOrder.verify(questionDao).findAll();
        inOrder.verify(questionConverter).convertQuestionToString(firstQuestion);
        inOrder.verify(ioService).readIntForRangeWithPromptAndErrorMessageLocalized(1, 2, question1AsString,
                "TestService.error.incorrect.answer.number");
        inOrder.verify(questionConverter).convertQuestionToString(secondQuestion);
        inOrder.verify(ioService).readIntForRangeWithPromptAndErrorMessageLocalized(1, 2, question2AsString,
                "TestService.error.incorrect.answer.number");
    }

    @DisplayName("Should print error message upon QuestionReadException")
    @Test
    void shouldPrintErrorMessageUponQuestionReadException() {
        var student = new Student("Ivan", "Ivanov");
        when(questionDao.findAll()).thenThrow(QuestionReadException.class);

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).isEmpty();
        assertThat(result.getRightAnswersCount()).isEqualTo(0);

        verify(ioService).printLineLocalized("TestService.error.with.questions");
        verifyNoInteractions(questionConverter);
        verify(ioService, never()).readIntForRangeWithPromptAndErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString());
    }

    @DisplayName("Should get wrong answer exception during get answer")
    @Test
    void shouldGetWrongAnswerUponExceptionDuringGetAnswer() {
        var student = new Student("Ivan", "Ivanov");
        var question = new Question("question1", List.of(
                new Answer("q1 answer1", true),
                new Answer("q1 answer2", true)));
        when(questionDao.findAll()).thenReturn(List.of(question));
        when(ioService.readIntForRangeWithPromptAndErrorMessageLocalized(anyInt(), anyInt(), anyString(), anyString()))
                .thenThrow(IllegalArgumentException.class);

        var result = testService.executeTestFor(student);

        assertThat(result.getAnsweredQuestions()).containsExactly(question);
        assertThat(result.getRightAnswersCount()).isEqualTo(0);

        verify(questionDao).findAll();
        verify(ioService).printLineLocalized("TestService.error.attempt.expired");
    }

}
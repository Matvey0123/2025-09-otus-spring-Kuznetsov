package ru.otus.hw.service;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;
import ru.otus.hw.exceptions.QuestionReadException;

import java.util.List;

import static org.mockito.Mockito.*;


class TestServiceImplTest {

    @InjectMocks
    private TestServiceImpl testService;

    @Mock
    private IOService ioService;

    @Mock
    private QuestionDao questionDao;

    @Mock
    private QuestionsConverter questionsConverter;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @DisplayName("Should exec dependencies methods in correct order")
    @Test
    void shouldExecDependenciesMethodsInCorrectOrder() {
        var firstQuestion = new Question("question1", List.of(
                new Answer("q1 answer1", true),
                new Answer("q1 answer2", false)));
        var secondQuestion = new Question("question2", List.of(
                new Answer("q2 answer1", true),
                new Answer("q2 answer2", false)));
        var questions = List.of(firstQuestion, secondQuestion);
        var questionsAsString = "q1, q2 as string";
        when(questionDao.findAll()).thenReturn(questions);
        when(questionsConverter.convertQuestionsToString(anyList())).thenReturn(questionsAsString);

        testService.executeTest();

        verify(questionDao).findAll();
        verify(questionsConverter).convertQuestionsToString(questions);
        verify(ioService).printLine(questionsAsString);
    }

    @DisplayName("Should print error message upon QuestionReadException")
    @Test
    void shouldPrintErrorMessageUponQuestionReadException() {
        when(questionDao.findAll()).thenThrow(QuestionReadException.class);

        testService.executeTest();

        verify(ioService).printLine("Error during generating questions. Sorry, testing is cancelled");
    }

}
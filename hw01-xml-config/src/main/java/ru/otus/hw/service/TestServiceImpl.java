package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.exceptions.QuestionReadException;

@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private final IOService ioService;

    private final QuestionDao questionDao;

    private final QuestionsConverter questionsConverter;

    @Override
    public void executeTest() {
        ioService.printLine("");
        ioService.printFormattedLine("Please answer the questions below%n");

        try {
            printQuestions();
        } catch (QuestionReadException e) {
            ioService.printLine("Error during generating questions. Sorry, testing is cancelled");
        }
    }

    private void printQuestions() {
        var questions = questionDao.findAll();
        var questionsAsString = questionsConverter.convertQuestionsToString(questions);
        ioService.printLine(questionsAsString);
    }
}

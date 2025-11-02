package ru.otus.hw.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.dao.QuestionDao;
import ru.otus.hw.domain.Question;
import ru.otus.hw.domain.Student;
import ru.otus.hw.domain.TestResult;
import ru.otus.hw.exceptions.QuestionReadException;

@Service
@RequiredArgsConstructor
public class TestServiceImpl implements TestService {

    private static final String INCORRECT_ANSWER_NUMBER_RETRY_MESSAGE_CODE =
            "TestService.error.incorrect.answer.number";

    private final LocalizedIOService ioService;

    private final QuestionDao questionDao;

    private final QuestionConverter questionConverter;

    @Override
    public TestResult executeTestFor(Student student) {
        ioService.printLine("");
        ioService.printLineLocalized("TestService.answer.the.questions");
        ioService.printLine("");

        var testResult = new TestResult(student);

        try {
            var questions = questionDao.findAll();
            questions.forEach(q -> getAndApplyAnswer(testResult, q));
        } catch (QuestionReadException e) {
            ioService.printLineLocalized("TestService.error.with.questions");
        }
        return testResult;
    }

    private void getAndApplyAnswer(TestResult testResult, Question question) {
        try {
            var answerList = question.answers();
            int answerNumber = readAnswerNumber(question);
            var isAnswerValid = answerList.get(answerNumber - 1).isCorrect();
            testResult.applyAnswer(question, isAnswerValid);
        } catch (Exception e) {
            ioService.printLineLocalized("TestService.error.attempt.expired");
            testResult.applyAnswer(question, false);
        }
    }

    private int readAnswerNumber(Question question) {
        var questionAsString = questionConverter.convertQuestionToString(question);
        return ioService.readIntForRangeWithPromptAndErrorMessageLocalized(1, question.answers().size(),
                questionAsString, INCORRECT_ANSWER_NUMBER_RETRY_MESSAGE_CODE);
    }

}

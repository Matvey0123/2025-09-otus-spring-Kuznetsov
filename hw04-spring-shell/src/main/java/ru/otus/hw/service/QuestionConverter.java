package ru.otus.hw.service;

import org.springframework.stereotype.Component;
import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

@Component
public class QuestionConverter {

    private static final String QUESTION_PRINT_FORMAT = "%s\n%s";

    private static final String ANSWER_PRINT_FORMAT = "\t%d. %s\n";

    public String convertQuestionToString(Question question) {
        var answers = getAnswersAsString(question.answers());
        return String.format(QUESTION_PRINT_FORMAT, question.text(), answers);
    }

    private String getAnswersAsString(List<Answer> answers) {
        return IntStream.range(0, answers.size())
                .mapToObj(i -> {
                    var answer = answers.get(i);
                    int answerNumber = i + 1;
                    return String.format(ANSWER_PRINT_FORMAT, answerNumber, answer.text());
                })
                .collect(Collectors.joining());
    }
}

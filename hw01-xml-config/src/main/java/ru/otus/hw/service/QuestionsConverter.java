package ru.otus.hw.service;

import ru.otus.hw.domain.Answer;
import ru.otus.hw.domain.Question;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

class QuestionsConverter {

    private static final String QUESTION_PRINT_FORMAT = "%d. %s\n%s";

    private static final String ANSWER_PRINT_FORMAT = "\t%d. %s\n";

    String convertQuestionsToString(List<Question> questions) {
        return IntStream.range(0, questions.size())
                .mapToObj(i -> {
                    var question = questions.get(i);
                    int questionNumber = i + 1;
                    var answers = getAnswersAsString(question.answers());
                    return String.format(QUESTION_PRINT_FORMAT, questionNumber, question.text(), answers);
                })
                .collect(Collectors.joining());
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

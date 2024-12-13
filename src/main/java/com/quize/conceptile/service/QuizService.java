package com.quize.conceptile.service;

import com.quize.conceptile.model.Question;
import com.quize.conceptile.model.UserAnswer;
import com.quize.conceptile.model.UserSession;
import com.quize.conceptile.repository.QuestionRepository;
import com.quize.conceptile.repository.UserAnswerRepository;
import com.quize.conceptile.repository.UserSessionRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Random;

@Service
public class QuizService {

    private final QuestionRepository questionRepository;
    private final UserSessionRepository userSessionRepository;
    private final UserAnswerRepository userAnswerRepository;

    public QuizService(QuestionRepository questionRepository, UserSessionRepository userSessionRepository, UserAnswerRepository userAnswerRepository) {
        this.questionRepository = questionRepository;
        this.userSessionRepository = userSessionRepository;
        this.userAnswerRepository = userAnswerRepository;
    }

    public UserSession startNewSession(String userId) {
        UserSession userSession = new UserSession();
        userSession.setUserId(userId);
        userSession.setTotalQuestionsAnswered(0);
        userSession.setCorrectAnswers(0);
        userSession.setIncorrectAnswers(0);

        return userSessionRepository.save(userSession);
    }

    public Optional<Question> getRandomQuestion() {
        List<Question> questions = questionRepository.findAll();
        if (questions.isEmpty()) {
            return Optional.empty();
        }
        Random random = new Random();
        return Optional.of(questions.get(random.nextInt(questions.size())));
    }

    public boolean submitAnswer(String userId, Long questionId, String selectedAnswer) {
        Optional<Question> questionOpt = questionRepository.findById(questionId);
        if (questionOpt.isEmpty()) {
            return false;
        }

        Question question = questionOpt.get();
        boolean isCorrect = question.getCorrectAnswer().equalsIgnoreCase(selectedAnswer);

        // Record the answer
        UserAnswer userAnswer = new UserAnswer();
        userAnswer.setUserId(userId);
        userAnswer.setQuestionId(questionId);
        userAnswer.setSelectedAnswer(selectedAnswer);
        userAnswer.setCorrect(isCorrect);
        userAnswerRepository.save(userAnswer);

        // Update UserSession based on the answer
        UserSession userSession = userSessionRepository.findByUserId(userId);
        if (isCorrect) {
            userSession.setCorrectAnswers(userSession.getCorrectAnswers() + 1);
        } else {
            userSession.setIncorrectAnswers(userSession.getIncorrectAnswers() + 1);
        }
        userSession.setTotalQuestionsAnswered(userSession.getTotalQuestionsAnswered() + 1);
        userSessionRepository.save(userSession);

        return isCorrect;
    }

    public UserSession getQuizResults(String userId) {
        return userSessionRepository.findByUserId(userId);
    }
}

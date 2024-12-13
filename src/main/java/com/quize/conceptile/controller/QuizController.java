package com.quize.conceptile.controller;

import com.quize.conceptile.model.Question;
import com.quize.conceptile.model.UserSession;
import com.quize.conceptile.service.QuizService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/quiz")
public class QuizController {
    private final QuizService quizService;

    public QuizController(QuizService quizService) {
        this.quizService = quizService;
    }

    @PostMapping("/start")
    public ResponseEntity<UserSession> startQuiz(@RequestParam(value = "userId") String userId) {
        return ResponseEntity.ok(quizService.startNewSession(userId));
    }


    @GetMapping("/question")
    public ResponseEntity<Question> getRandomQuestion() {
        return quizService.getRandomQuestion()
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.noContent().build());
    }

    @PostMapping("/submit")
    public ResponseEntity<String> submitAnswer(
            @RequestParam(value = "userId") String userId,
            @RequestParam(value = "questionId") Long questionId,
            @RequestParam(value = "answer") String answer) {

        boolean isCorrect = quizService.submitAnswer(userId, questionId, answer);
        return ResponseEntity.ok(isCorrect ? "Correct Answer!" : "Incorrect Answer.");
    }


    @GetMapping("/results")
    public ResponseEntity<UserSession> getResults(@RequestParam(value ="userId") String userId) {
        return ResponseEntity.ok(quizService.getQuizResults(userId));
    }
}

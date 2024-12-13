package com.quize.conceptile.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.quize.conceptile.model.Question;

public interface QuestionRepository extends JpaRepository<Question, Long> {

}

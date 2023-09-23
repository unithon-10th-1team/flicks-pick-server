package com.flickspick.question.repository;

import java.util.List;

import com.flickspick.question.domain.QuestionAnswer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface QuestionAnswerRepository extends JpaRepository<QuestionAnswer, Long> {
	List<QuestionAnswer> findAllByQuestionId(Long id);
}

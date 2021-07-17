package com.flex.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.Answer;

@Repository
public interface AnswerRepository extends JpaRepository<Answer, Long> {
	
	public Answer findByQuestionIdAndUserId(Long questionId, Long userId);
	
	public boolean existsByUserIdAndQuestionId(Long userId, Long questionId);
}

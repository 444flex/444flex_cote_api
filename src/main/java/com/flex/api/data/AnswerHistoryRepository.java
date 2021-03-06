package com.flex.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.AnswerHistory;

@Repository
public interface AnswerHistoryRepository extends JpaRepository<AnswerHistory, Long>{
	
	public List<AnswerHistory> findByAnswerId(Long answerId);

}

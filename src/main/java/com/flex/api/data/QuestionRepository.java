package com.flex.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.Question;

@Repository
public interface QuestionRepository extends JpaRepository<Question, Long>{
	
}

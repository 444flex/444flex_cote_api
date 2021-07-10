package com.flex.api.data;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flex.api.model.BaseEntity;
import com.flex.api.model.Question;
import com.flex.api.model.User;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class FlexTxManager {

	@PersistenceContext
	protected EntityManager entityManager;
	
	private final UserRepository userRepository;
	private final QuestionRepository questionRepository;
	
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public void refresh(BaseEntity entity) {
		entityManager.refresh(entity);
	}
	
	public List<Question> findQuestionAll() {
		return questionRepository.findAll();
	}
	
	public Question findQuestionById(Long id) {
		return questionRepository.findById(id).get();
	}
	
	public User findUserByNameAndCellNumber(String name, String cellNumber) {
		return userRepository.findByNameAndCellNumber(name, cellNumber);
	}
}

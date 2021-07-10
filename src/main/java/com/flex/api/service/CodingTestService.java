package com.flex.api.service;

import java.util.List;

import com.flex.api.dto.request.UserReqDto;
import com.flex.api.model.Question;
import com.flex.api.model.User;

public interface CodingTestService {
	
	public User getUser(UserReqDto userReqDto);
	
	public List<Question> getQuestionList();
	
	public Question getQuestion(Long id);
	
	
}

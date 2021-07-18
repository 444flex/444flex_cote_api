package com.flex.api.service;

import java.util.List;

import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.Verification;
import com.flex.api.model.VerificationParam;

public interface QuestionService {

	public Question getQuestion(Long questionId);
	
	public QuestionResDto getQuestion(Long userId, Long questionId);
	
	public List<Question> getQuestionList(Long userId);
	
	public List<Parameter> getParameterList(Long questionId);
	
	public List<Verification> getVerificationList(Long id);
	
	public VerificationParam getVerificationParam(Long verificationId, Long parameterId);
}

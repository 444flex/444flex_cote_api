package com.flex.api.service;

import java.util.List;

import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.AnswerCheckResDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.model.Answer;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.User;
import com.flex.api.model.Verification;

public interface CodingTestService {
	
	public User getUser(Long id);

	public User getUser(UserReqDto userReqDto);
	
	public Question getQuestion(Long questionId);
	
	public QuestionResDto getQuestion(Long userId, Long questionId);
	
	public List<Question> getQuestionList();
	
	public Answer getAnswer(Long userId, Long questionId);
	
	public AnswerCheckResDto checkSubmitAnswer(Long userId, Long questionId);
	
	public List<Parameter> getParameterList(Long questionId);
	
	public List<Verification> getVerificationList(Long id); 
	
	public AnswerResDto submitAnswer(Long userId, AnswerReqDto answerReqDto);
	
}

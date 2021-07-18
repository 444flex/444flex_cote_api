package com.flex.api.service;

import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.response.AnswerCheckResDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.AnswerSubmitResDto;
import com.flex.api.model.Answer;

public interface AnswerService {

	public Answer getAnswer(Long userId, Long questionId);
	
	public AnswerSubmitResDto checkSubmitAnswer(Long userId, Long questionId);
	
	public AnswerResDto insertAnswer(Long userId, AnswerReqDto answerReqDto);
	
	public AnswerSubmitResDto updateAnswer(Long userId, AnswerReqDto answerReqDto);
}

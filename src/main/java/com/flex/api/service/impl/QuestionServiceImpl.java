package com.flex.api.service.impl;

import java.util.List;

import org.springframework.beans.BeanUtils;
import org.springframework.stereotype.Service;

import com.flex.api.data.AnswerRepository;
import com.flex.api.data.ParameterRepository;
import com.flex.api.data.QuestionRepository;
import com.flex.api.data.VerificationParamRepository;
import com.flex.api.data.VerificationRepository;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.exception.EntityNotFoundException;
import com.flex.api.model.Answer;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.User;
import com.flex.api.model.Verification;
import com.flex.api.model.VerificationParam;
import com.flex.api.service.QuestionService;
import com.flex.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class QuestionServiceImpl implements QuestionService {
	
	private final QuestionRepository questionRepository;
	private final AnswerRepository answerRepository;
	private final ParameterRepository parameterRepository;
	private final VerificationRepository verificationRepository;
	private final VerificationParamRepository verificationParamRepository;

	private final UserService userService;
	
	@Override
	public Question getQuestion(Long questionId) {
		if (questionRepository.existsById(questionId)) {
			return questionRepository.findById(questionId).get();
		} else {
			throw new EntityNotFoundException("Question", "Question is not found. id:" + questionId,  null);
		}
	}
	
	@Override
	public QuestionResDto getQuestion(Long userId, Long questionId) {
		User user = userService.getUser(userId);
		QuestionResDto questionResDto = new QuestionResDto();
		Question question = this.getQuestion(questionId);
		List<Parameter> parameterList = this.getParameterList(questionId);
		Answer answer = answerRepository.findByUserIdAndQuestionId(user.getId(), questionId);
		BeanUtils.copyProperties(question, questionResDto);
		if (answer == null) {
			String defaultCode = this.getDefaultCode(question, parameterList);
			questionResDto.setCode(defaultCode);
		} else {
			questionResDto.setCode(answer.getCode());
		}
		
		return questionResDto;
	}
	
	private String getDefaultCode(Question question, List<Parameter> parameterList) {
		StringBuilder sb = new StringBuilder();
		sb.append("public class Solution {\n");
		sb.append("\tpublic").append(" ").append(question.getReturnType()).append(" ").append(question.getMethodName()).append(" ").append("(");
		for (int i=0; i<parameterList.size(); i++ ){
			sb.append(parameterList.get(i).getType()).append(" ").append(parameterList.get(i).getName());
			if (i < parameterList.size()-1) sb.append(", ");
		}
		sb.append(") {").append("\n");
		sb.append("\t\treturn null;\n");
		sb.append("\t}\n}");
		return sb.toString();
	}
	
	@Override
	public List<Question> getQuestionList(Long userId) {
		userService.getUser(userId);
		return questionRepository.findAll();
	}
	
	@Override
	public List<Parameter> getParameterList(Long questionId) {
		return parameterRepository.findByQuestionId(questionId);
	}
	
	@Override
	public List<Verification> getVerificationList(Long id) {
		return verificationRepository.findByQuestionId(id);
	}
	
	@Override
	public VerificationParam getVerificationParam(Long verificationId, Long parameterId) {
		return verificationParamRepository.findByVerificationIdAndParameterId(verificationId, parameterId);
	}
}

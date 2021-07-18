package com.flex.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flex.api.data.AnswerHistoryRepository;
import com.flex.api.data.AnswerRepository;
import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.response.AnswerCheckResDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.AnswerResDto.TestCase;
import com.flex.api.dto.response.AnswerSubmitResDto;
import com.flex.api.exception.DirectoryCreateFailedException;
import com.flex.api.exception.EntityNotFoundException;
import com.flex.api.exception.EntityNotModifyException;
import com.flex.api.exception.FileCreateFailedException;
import com.flex.api.model.Answer;
import com.flex.api.model.AnswerHistory;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.User;
import com.flex.api.model.Verification;
import com.flex.api.model.VerificationParam;
import com.flex.api.service.AnswerService;
import com.flex.api.service.QuestionService;
import com.flex.api.service.UserService;
import com.flex.api.strategy.IntegerArrayStrategy;
import com.flex.api.strategy.IntegerStrategy;
import com.flex.api.strategy.ObjectStrategy;
import com.flex.api.strategy.StringArrayStrategy;
import com.flex.api.strategy.StringStrategy;
import com.flex.api.util.CmdUtil;
import com.flex.api.util.FileUtil;
import com.flex.api.util.ReflectionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AnswerServiceImpl implements AnswerService {
	
	@Value("${flex.class.path}")
	private String classPath;
	
	@Value("${flex.class.name}")
	private String className;
	
	@Value("${flex.class.extension}")
	private String classExtension;
	
	@Value("${flex.method.name}")
	private String methodName;
	
	@Value("${flex.thread.size:10}")
	private Integer threadSize;

	private final AnswerRepository answerRepository;
	private final AnswerHistoryRepository answerHistoryRepository;
	
	private final UserService userService;
	private final QuestionService questionService;
	
	@Override
	public Answer getAnswer(Long userId, Long questionId) {
		return answerRepository.findByUserIdAndQuestionId(userId, questionId);
	}
	
	private Answer getAnswer(Long id) {
		return answerRepository.findById(id).get();
	}

	@Override
	public AnswerSubmitResDto checkSubmitAnswer(Long userId, Long questionId) {
		User user = userService.getUser(userId);
		Question question = questionService.getQuestion(questionId);
		Answer answer = this.getAnswer(user.getId(), question.getId());
		AnswerSubmitResDto answerSubmitResDto = new AnswerSubmitResDto();
		if (answer == null) {
			throw new EntityNotFoundException("Answer", "Answer is not found. user:" + userId, null);
		} else {
			BeanUtils.copyProperties(answer, answerSubmitResDto);
		}
		
		return answerSubmitResDto;
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public AnswerResDto insertAnswer(Long userId, AnswerReqDto answerReqDto) {
		
		Question question = questionService.getQuestion(answerReqDto.getQuestionId());
		return this.getScoreCode(answerReqDto, userId, question);
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public AnswerSubmitResDto updateAnswer(Long userId, AnswerReqDto answerReqDto) {
		AnswerResDto answerResDto = this.insertAnswer(userId, answerReqDto);
		Answer answer = this.getAnswer(answerResDto.getAnswerId());
		answer = this.submitAnswer(answer);
		AnswerSubmitResDto resDto = new AnswerSubmitResDto();
		BeanUtils.copyProperties(answer, resDto);
		return resDto;
	}
	
	public Answer submitAnswer(Answer answer) {
		answer.setSubmitYn(true);
		return answerRepository.save(answer);
	}
	
	public AnswerResDto getScoreCode(AnswerReqDto answerReqDto, Long userId, Question question) {

		User user = userService.getUser(userId);
		String path = this.classPath + user.getId() + "/" + question.getId() + "" + "/" + System.currentTimeMillis() + "/";
		
		this.saveFile(answerReqDto.getCode(), path);
		this.compileCode(path);

		return this.verify(question, user, answerReqDto.getCode(), path);
	}
	
	private AnswerResDto verify(Question question, User user, String code, String path) {
		String url = path + this.className + this.classExtension;
		Answer answer = this.getAnswer(user.getId(), question.getId());
		if (answer != null && answer.isSubmitYn())
			throw new EntityNotModifyException("Answer", "Answer is already submitted. Answer:" + answer.getId(), null);
		List<Verification> verificationList = questionService.getVerificationList(question.getId());
		List<Parameter> parameters = questionService.getParameterList(question.getId());
		AnswerResDto answerResDto = new AnswerResDto();
		for (Verification verification : verificationList) {
			try {
//				ExecutorService executor = Executors.newSingleThreadExecutor();
//				Callable task = new Callable() {
//					
//				};
				
				Object correctAnswer = null;
				Object userAnswer = null;
				List<Object> paramList = new ArrayList<Object>();
				correctAnswer = this.setObjectStrategy(question.getReturnType(), verification.getCorrectAnswer(), question.getReturnType2().name());
				userAnswer = this.setObjectStrategy(question.getReturnType(), verification.getCorrectAnswer(), question.getReturnType2().name());
				
				for (Parameter param : parameters) {
					VerificationParam vp = questionService.getVerificationParam(verification.getId(), param.getId());
					paramList.add(this.setObjectStrategy(param.getType(), vp.getValue(), param.getType2().name()));
				}
				
				Class<?>[] classes = ReflectionUtil.listToArray(paramList);
				ReflectionUtil reflection = new ReflectionUtil.Builder()
						.fileDir(path)
						.fileName(this.className)
						.methodName(this.methodName)
						.classes(classes)
						.build();
				
				long s = System.currentTimeMillis();
				userAnswer = reflection.execMethod(paramList.toArray(new Object[paramList.size()]));
				long e = System.currentTimeMillis();
				
				if (Objects.deepEquals(correctAnswer, userAnswer)) {
					System.out.println("true");
					answerResDto.setTestCase(true, e-s);
				} else {
					System.out.println("false");
					answerResDto.setTestCase(false, e-s);
				}
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("ClassNotFoundException");
			}
		}
		
		int score = 0;
		
		for (TestCase testCase : answerResDto.getTestCaseList()) {
			if (testCase.isCompileYn()) score++;
		}
		
		score = score * 100 / answerResDto.getTestCaseList().size();
		answerResDto.setScore(score);
		
		if (answer == null) {
			answer = new Answer();
			answer.setScore(score);
			answer.setFileName(url);
			answer.setCode(code);
			answer.setQuestion(question);
			answer.setSubmitCount(0);
			answer.setUser(user);
		} else if (answer.getScore() <= score) {
			answer.setScore(score);
			answer.setFileName(url);
			answer.setCode(code);
		}
		
		answer.addSubmitCount();
		
		AnswerHistory answerHis = new AnswerHistory();
		answerHis.setAnswerHistory(answer, score, url, code);
		
		if (answer != null ) answerRepository.save(answer);
		answerHistoryRepository.save(answerHis);
		
		answerResDto.setAnswerId(answer.getId());
		
		return answerResDto;
	}
	
	private void compileCode(String path) {
		CmdUtil.compile(path, this.className);
	}
	
	private void saveFile(String code, String path) {
		try {
			File folder = new File(path);
			if (!folder.exists())
				folder.mkdirs();
		} catch (Exception e) {
			throw new DirectoryCreateFailedException("Directory", "Directory path:" + path, null);
		}
		
		try {
			FileUtil.saveFile(path + this.className + this.classExtension, code);
		} catch (IOException e) {
			throw new FileCreateFailedException("File", "File path:" + path + this.className + this.classExtension, null);
		}
	}
	
	private Object setObjectStrategy(String type, String value, String type2) throws ClassNotFoundException {
		ObjectStrategy object = null;
		if (type2.equals(Parameter.Type2.single.name())) {
			if (type.equals("int") || type.equals("Integer")) {
				object = new IntegerStrategy();
			} else if (type.equals("String")) {
				object = new StringStrategy();
			}
		} else {
			if (type.equals("int[]")) {
				object = new IntegerArrayStrategy();
			} else if (type.equals("String[]")) {
				object = new StringArrayStrategy();
			}
		}
		return object.getTypeAndValue(value);
	}
}
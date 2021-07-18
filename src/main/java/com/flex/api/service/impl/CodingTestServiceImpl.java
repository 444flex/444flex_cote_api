package com.flex.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flex.api.data.AnswerHistoryRepository;
import com.flex.api.data.AnswerRepository;
import com.flex.api.data.ParameterRepository;
import com.flex.api.data.QuestionRepository;
import com.flex.api.data.UserRepository;
import com.flex.api.data.VerificationParamRepository;
import com.flex.api.data.VerificationRepository;
import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.AnswerCheckResDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.AnswerResDto.TestCase;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.exception.CompileErrorException;
import com.flex.api.exception.DirectoryCreateFailedException;
import com.flex.api.exception.EntityNotFoundException;
import com.flex.api.exception.FileCreateFailedException;
import com.flex.api.model.Answer;
import com.flex.api.model.AnswerHistory;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.User;
import com.flex.api.model.Verification;
import com.flex.api.model.VerificationParam;
import com.flex.api.service.CodingTestService;
import com.flex.api.util.CmdUtil;
import com.flex.api.util.FileUtil;
import com.flex.api.util.ReflectionUtil;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class CodingTestServiceImpl implements CodingTestService {
	
	private final QuestionRepository questionRepository;
	private final UserRepository userRepository;
	private final ParameterRepository parameterRepository;
	private final VerificationRepository verificationRepository;
	private final VerificationParamRepository verificationParamRepository;
	private final AnswerRepository answerRepository;
	private final AnswerHistoryRepository answerHistoryRepository;
	
	@Value("${flex.class.path}")
	private String classPath;
	
	@Value("${flex.class.name}")
	private String className;
	
	@Value("${flex.class.extension}")
	private String classExtension;
	
	@Value("${flex.method.name}")
	private String methodName;
	
	@Override
	public User getUser(Long id) {
		if (userRepository.existsById(id)) {
			return userRepository.findById(id).get();
		} else {
			throw new EntityNotFoundException("User", "User is not found. id:" + id,  null);
		}
	}
	
	@Override
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public User getUser(UserReqDto userReqDto) {
		if (userRepository.existsByNameAndCellNumber(userReqDto.getName(), userReqDto.getCellNumber())) {
			User user = userRepository.findByNameAndCellNumber(userReqDto.getName(), userReqDto.getCellNumber());
			user = this.updateUserFirstLoginTime(user);
			return user;
		} else {
			throw new EntityNotFoundException("User", "User is not found. name:" + userReqDto.getName(),  null);
		}
	}
	
	public User updateUserFirstLoginTime(User user) {
		if (user.getFirstLoginTime() == null) {
			user.setFirstLoginTime();
			return userRepository.save(user);
		}
		return user;
	}
	
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
		QuestionResDto questionResDto = new QuestionResDto();
		Question question = this.getQuestion(questionId);
		List<Parameter> parameterList = this.getParameterList(questionId);
		Answer answer = this.getAnswer(userId, questionId);
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
	public List<Question> getQuestionList() {
		return questionRepository.findAll();
	}
	
	@Override
	public Answer getAnswer(Long userId, Long questionId) {
		return answerRepository.findByQuestionIdAndUserId(questionId, userId);
	}
	
	@Override
	public AnswerCheckResDto checkSubmitAnswer(Long userId, Long questionId) {
		User user = this.getUser(userId);
		Question question = this.getQuestion(questionId);
		return new AnswerCheckResDto(answerRepository.existsByUserIdAndQuestionId(user.getId(), question.getId()));
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
	@Transactional(propagation = Propagation.REQUIRED, isolation = Isolation.READ_COMMITTED, rollbackFor = Exception.class)
	public AnswerResDto submitAnswer(Long userId, AnswerReqDto answerReqDto) {
		
		Question question = this.getQuestion(answerReqDto.getQuestionId());
		return this.getScoreCode(answerReqDto, userId, question);
	}
	
	public AnswerResDto getScoreCode(AnswerReqDto answerReqDto, Long userId, Question question) {

		User user = this.getUser(userId);
		String path = this.classPath + user.getId() + "/" + question.getId() + "" + "/" + System.currentTimeMillis() + "/";
		
		this.saveFile(answerReqDto.getCode(), path);
		this.compileCode(path);

		return this.verify(question, user, answerReqDto.getCode(), path);
	}
	
	private AnswerResDto verify(Question question, User user, String code, String path) {
		String url = path + this.className + this.classExtension;
		Answer answer = this.getAnswer(user.getId(), question.getId());
		List<Verification> verificationList = this.getVerificationList(question.getId());
		List<Parameter> parameters = this.getParameterList(question.getId());
		AnswerResDto answerResDto = new AnswerResDto();
		for (Verification verification : verificationList) {
			try {
				Object correctAnswer = null;
				Object userAnswer = null;
				List<Object> paramList = new ArrayList<Object>();
				correctAnswer = declareObject(question.getReturnType(), verification.getCorrectAnswer(), question.getReturnType2().name());
				userAnswer = declareObject(question.getReturnType(), verification.getCorrectAnswer(), question.getReturnType2().name());
				
				for (Parameter param : parameters) {
					VerificationParam vp = verificationParamRepository.findByVerificationIdAndParameterId(verification.getId(), param.getId());
					paramList.add(declareObject(param.getType(), vp.getValue(), param.getType2().name()));
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
	
	private Object declareSingle(String type, String value) {
		if (type.equals("int") || type.equals("Integer")) {
			return (int)Integer.parseInt(value); 
		} else if (type.equals("String")) {
			return value;
		}
		return value;
	}
	
	private Object declareArray(String type, String value) throws ClassNotFoundException {
		Object[] copy = null;
		Object copy2 = null;
		String[] valueArray = value.replace("{", "").replace("}", "").split(",");
		if (type.equals("int[]")) {
			Class<?> cls = Class.forName("java.lang.Integer");
			Object array = Array.newInstance(cls, valueArray.length);
			Class<?> arrayType = array.getClass();
			copy = Arrays.copyOf((Object[])arrayType.cast(array), Array.getLength(array));
			for (int i=0; i<valueArray.length; i++) {
				copy[i] = Integer.parseInt(valueArray[i]);
			}
			copy2 = Arrays.stream(copy).mapToInt(i->(int)i).toArray();
			return copy2;
		} else if (type.equals("String[]")) {
			Class<?> cls = Class.forName("java.lang.String");
			Object array = Array.newInstance(cls, valueArray.length);
			Class<?> arrayType = array.getClass();
			copy = Arrays.copyOf((Object[])arrayType.cast(array), Array.getLength(array));
			for (int i=0; i<valueArray.length; i++) {
				copy[i] = valueArray[i];
			}
		}
		return copy;
	}
	
	private Object declareObject(String type, String value, String type2) throws ClassNotFoundException {
		
		if (type2.equals("single")) {
			return declareSingle(type, value);
		} else {
			return declareArray(type, value);
		}
	}
}

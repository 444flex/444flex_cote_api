package com.flex.api.service.impl;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flex.api.data.AnswerHistoryRepository;
import com.flex.api.data.AnswerRepository;
import com.flex.api.data.FlexTxManager;
import com.flex.api.data.ParameterRepository;
import com.flex.api.data.QuestionRepository;
import com.flex.api.data.UserRepository;
import com.flex.api.data.VerificationParamRepository;
import com.flex.api.data.VerificationRepository;
import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.model.Answer;
import com.flex.api.model.AnswerHistory;
import com.flex.api.model.Parameter;
import com.flex.api.model.Question;
import com.flex.api.model.Result;
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
	
//	@Transactional(propagation = Propagation.REQUIRED)
//	public void updateTimeList(List<Question> list) {
//		for (Question s : list) {
//			this.updateTime(s);
//		}
//	}
//	
//	@Transactional(propagation = Propagation.REQUIRES_NEW)
//	public void updateTime(Question q) {
//		return repository.save(q);
//	}
//	private final FlexTxManager flexTxManager;
	
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
			throw new RuntimeException("No entity exception");
		}
	}
	
	@Override
	public User getUser(UserReqDto userReqdto) {
		if (userRepository.existsByNameAndCellNumber(userReqdto.getName(), userReqdto.getCellNumber())) {
			return userRepository.findByNameAndCellNumber(userReqdto.getName(), userReqdto.getCellNumber());
		} else {
			throw new RuntimeException("No entity exception");
		}
	}
	
	@Override
	public Question getQuestion(Long questionId) {
		if (questionRepository.existsById(questionId)) {
			return questionRepository.findById(questionId).get();
		} else {
			throw new RuntimeException("No entity exception");
		}
	}
	
	@Override
	public QuestionResDto getQuestion(Long userId, Long questionId) {
		QuestionResDto questionResDto = new QuestionResDto();
		Question question = this.getQuestion(questionId);
		List<Parameter> parameterList = this.getParameterList(questionId);
		Answer answer = this.getAnswer(userId, questionId);
		
		BeanUtils.copyProperties(question, questionResDto);
		StringBuilder sb = new StringBuilder();
		if (answer == null) {
			sb.append("public").append("\s").append(question.getReturnType()).append("\s").append(question.getMethodName()).append("\s").append("(");
			for (int i=0; i<parameterList.size(); i++ ){
				sb.append(parameterList.get(i).getType()).append("\s").append(parameterList.get(i).getName());
				if (i < parameterList.size()-1) sb.append(", \s");
			}
			sb.append(")\s{").append("\n");
			sb.append("return null;\n");
			sb.append("}");
			questionResDto.setCode(sb.toString());
		} else {
			questionResDto.setCode(answer.getCode());
		}
		
		return questionResDto;
	}
	
	@Override
	public List<Question> getQuestionList() {
		return questionRepository.findAll();
	}
	
	@Override
	public Answer getAnswer(Long questionId, Long userId) {
		return answerRepository.findByQuestionIdAndUserId(questionId, userId);
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
	public List<AnswerResDto> submitAnswer(AnswerReqDto answerReqDto) {
		
		User user = this.getUser(answerReqDto.getUserId());
		Question question = this.getQuestion(answerReqDto.getQuestionId());
		Answer answer = this.getAnswer(question.getId(), user.getId());
		List<Verification> verificationList = this.getVerificationList(question.getId());
		List<AnswerResDto> list = this.getScoreCode(answerReqDto.getCode(), question, verificationList, answer, user);
		// 해당 
		
		return list;
	}

	public List<AnswerResDto> getScoreCode(String code, Question question, List<Verification> verificationList, Answer answer, User user) {
		/*
		 * 파일 저장
		 */
		String path = this.classPath + user.getId() + "/" + question.getId() + "" + "/" + System.currentTimeMillis() + "/";
		String url = path + this.className + this.classExtension;
		try {
			File folder = new File(path);
			if (!folder.exists())
				folder.mkdirs();
			FileUtil.saveFile(url, code);
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		/*
		 * class 만들기
		 */
		try {
			CmdUtil.compile(path, this.className);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		List<AnswerResDto> list = new ArrayList<AnswerResDto>();
		List<Parameter> parameters = this.getParameterList(question.getId());
		
		int score = 0;
		
		for (Verification verification : verificationList) {
			try {
				Object correctAnswer = null;
				Object userAnswer = null;
				List<Object> paramList = new ArrayList<Object>();
				correctAnswer = declareArray(question.getReturnType(), verification.getCorrectAnswer());
				userAnswer = declareArray(question.getReturnType(), verification.getCorrectAnswer());
				
				for (Parameter param : parameters) {
					VerificationParam vp = verificationParamRepository.findByVerificationIdAndParameterId(verification.getId(), param.getId());
					paramList.add(declareArray(param.getType(), vp.getValue()));
				}
				
				
				int[] paramtest = new int[2];
				ReflectionUtil reflection = new ReflectionUtil.Builder()
						.fileDir(this.classPath)
						.fileName(this.className)
						.methodName(this.methodName)
						.classes(paramList.get(0).getClass(), paramList.get(1).getClass())
//						.classes(paramtest.getClass(), paramtest.getClass())
						.build();
				
				long s = System.currentTimeMillis();
				userAnswer = reflection.execMethod(paramList.get(0), paramList.get(1));
				long e = System.currentTimeMillis();
				AnswerResDto answerResDto = new AnswerResDto();
				if (Objects.deepEquals(correctAnswer, userAnswer)) {
					System.out.println("true");
					answerResDto.setCompileYn(true);
				} else {
					System.out.println("false");
					answerResDto.setCompileYn(false);
				}
				answerResDto.setCompileTime(e-s);
				
				list.add(answerResDto);
				
			} catch (ClassNotFoundException e) {
				throw new RuntimeException("Type convert error");
			}
		}
		
		if (answer == null) {
			answer = new Answer();
			answer.setScore(score);
			answer.setCompileTime(list.get(0).getCompileTime());
			answer.setCompileYn(list.get(0).isCompileYn());
			answer.setFileName(url);
			answer.setCode(code);
			answer.setQuestion(question);
			answer.setSubmitCount(1);
			answer.setUser(user);
		} else if (answer.getScore() < score) {
			answer.setScore(score);
			answer.setCompileTime(list.get(0).getCompileTime());
			answer.setCompileYn(list.get(0).isCompileYn());
			answer.setFileName(url);
			answer.setCode(code);
			answer.setSubmitCount(answer.getSubmitCount()+1);
		} else {
			
		}
		
		AnswerHistory answerHis = new AnswerHistory();
		answerHis.setAnswer(answer);
		answerHis.setScore(score);
		answerHis.setCompileTime(list.get(0).getCompileTime());
		answerHis.setCompileYn(list.get(0).isCompileYn());
		answerHis.setFileName(url);
		answerHis.setCode(code);
		
		if (answer != null ) answerRepository.save(answer);
		answerHistoryRepository.save(answerHis);
		
		
//		List<int[]> param1List = new ArrayList<int[]>();
//		List<int[]> param2List = new ArrayList<int[]>();
//		List<int[]> result1List = new ArrayList<int[]>();
//		
//		int[] param1 = null;
//		int[] param2 = null;
//		int[] result1Array = null;
//		
//		
//		param1 = new int[]{2,3,4,5,7,8};
//		param1List.add(param1);
//		param1 = new int[]{0,0,3,4,5,6};
//		param1List.add(param1);
//		param1 = new int[]{0,0,0,0,0,0};
//		param1List.add(param1);
//		
//		param2 = new int[]{31,10,45,1,6,19};
//		param2List.add(param1);
//		param2 = new int[]{7,8,9,10,11,12};
//		param2List.add(param1);
//		param2 = new int[]{23,1,5,22,12,31};
//		param2List.add(param1);
//		
//		result1Array = new int[] {6,6};
//		result1List.add(result1Array);
//		result1Array = new int[] {5,6};
//		result1List.add(result1Array);
//		result1Array = new int[] {1,6};
//		result1List.add(result1Array);
		
		
		/*
		 * class 실행
		 */
		/*
		ReflectionUtil reflection = new ReflectionUtil.Builder()
				.fileDir(this.classPath)
				.fileName(this.className)
				.methodName(this.methodName)
				.classes(param1.getClass(), param2.getClass())
				.build();
		
		List<AnswerResDto> list = new ArrayList<AnswerResDto>();
		
		for (int i=0; i<param1List.size(); i++) {
			int[] paramA = param1List.get(i);
			int[] paramB = param2List.get(i);
			
			long s = System.currentTimeMillis();
			int[] ans = (int[]) reflection.execMethod(paramA, paramB);
			long e = System.currentTimeMillis();
			
			AnswerResDto answerResDto = new AnswerResDto();
			
			if (ans[0] == result1List.get(i)[0] && ans[1] == result1List.get(i)[1]) {
				answerResDto.setCompileYn(true);
			} else {
				answerResDto.setCompileYn(false);
			}
//			result.setAnswer(ans);
			answerResDto.setCompileTime(e-s);
//			result.setTimemils(e-s);
			list.add(answerResDto);
		}
		*/
		return list;
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
}

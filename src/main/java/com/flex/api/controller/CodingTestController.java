package com.flex.api.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.model.Question;
import com.flex.api.model.Result;
import com.flex.api.model.User;
import com.flex.api.service.impl.CodingTestServiceImpl;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flex")
@Api(tags = "Test", description = "Test Service APIs", consumes = "application/json", produces = "application/json")
public class CodingTestController {

	private final Environment env;
	private final CodingTestServiceImpl service;
	
	@ApiOperation(value = "로그인", notes = "로그인")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "조회 성공"),
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"),
			@ApiResponse(code = 401, message = "사용자 인증 실패"),
			@ApiResponse(code = 403, message = "사용자 인증 만료, 접근권한 제한"),
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"),
			@ApiResponse(code = 500, message = "시스템 장애") })
	@PostMapping("/user")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<User> login(
			@RequestBody UserReqDto userReqDto
			) {
		User user = service.getUser(userReqDto);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@ApiOperation(value = "문제 목록 조회", notes = "문제 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "조회 성공"),
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"),
			@ApiResponse(code = 401, message = "사용자 인증 실패"),
			@ApiResponse(code = 403, message = "사용자 인증 만료, 접근권한 제한"),
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"),
			@ApiResponse(code = 500, message = "시스템 장애") })
	@GetMapping("/questions")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<Question>> getQuestionList(
			@RequestHeader(value = "user_id") Long userId
			) {
		List<Question> list = service.getQuestionList();
		return new ResponseEntity<List<Question>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "문제 상세 조회", notes = "문제 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "조회 성공"),
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"),
			@ApiResponse(code = 401, message = "사용자 인증 실패"),
			@ApiResponse(code = 403, message = "사용자 인증 만료, 접근권한 제한"),
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"),
			@ApiResponse(code = 500, message = "시스템 장애") })
	@GetMapping("/question/{question_id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<QuestionResDto> getQuestionList(
			@RequestHeader(value = "user_id") Long userId,
			@PathVariable("question_id") Long questionId
			) {
		QuestionResDto questionResDto = service.getQuestion(userId, questionId);
		return new ResponseEntity<QuestionResDto>(questionResDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "답변 제출", notes = "답변 제출")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "조회 성공"),
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"),
			@ApiResponse(code = 401, message = "사용자 인증 실패"),
			@ApiResponse(code = 403, message = "사용자 인증 만료, 접근권한 제한"),
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"),
			@ApiResponse(code = 500, message = "시스템 장애") })
	@PostMapping("/answer")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<AnswerResDto>> submitAnswer(
			@RequestHeader(value = "user_id") Long userId,
			@Valid @RequestBody AnswerReqDto answerReqDto
			) {
		List<AnswerResDto> rtn = service.submitAnswer(userId, answerReqDto);
		return new ResponseEntity<List<AnswerResDto>>(rtn, HttpStatus.OK);
	}
	
//	@ApiOperation(value = "테스트", notes = "테스트 api")
//	@ApiResponses(value = { //
//			@ApiResponse(code = 200, message = "조회 성공"), //
//			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"), //
//			@ApiResponse(code = 404, message = "정보가 존재하지 않음"), //
//			@ApiResponse(code = 500, message = "시스템 장애") })
//	@PostMapping("/codingTest")
//	@ResponseStatus(value = HttpStatus.OK)
//	public ResponseEntity<List<Result>> getScoreCode(
//			@Valid @RequestBody String code,
//			@RequestHeader(value = "X-Auth-Token", required = true) String authToken) {
//		List<Result> rtn = service.submitAnswer(code);
//		return new ResponseEntity<List<Result>>(rtn, HttpStatus.OK);
//	}
	
	@ApiOperation(value = "현재 시간", notes = "현재 시간 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "조회 성공"), //
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"), //
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"), //
			@ApiResponse(code = 500, message = "시스템 장애") })
	@GetMapping("/time")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Long> getTime() {
		return new ResponseEntity<Long>(new Date().getTime(), HttpStatus.OK);
	}

	@ApiOperation(value = "현재 프로파일", notes = "프로파일 리턴 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "조회 성공"), //
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"), //
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"), //
			@ApiResponse(code = 500, message = "시스템 장애") })
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public String getProfile(){
		return Arrays.stream(env.getActiveProfiles())
				.findFirst()
				.orElse("");
	}
}

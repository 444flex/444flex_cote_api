package com.flex.api.controller;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flex.api.annotation.RequiredToken;
import com.flex.api.dto.request.AnswerReqDto;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.AnswerResDto;
import com.flex.api.dto.response.AnswerSubmitResDto;
import com.flex.api.dto.response.QuestionResDto;
import com.flex.api.model.Question;
import com.flex.api.model.User;
import com.flex.api.service.AnswerService;
import com.flex.api.service.QuestionService;
import com.flex.api.service.UserService;
import com.flex.api.util.ErrorUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@CrossOrigin("*")
@RequestMapping("/flex")
@Api(tags = "Test", description = "Test Service APIs", consumes = "application/json", produces = "application/json")
public class CodingTestController {

	private final Environment env;
	private final UserService userService;
	private final QuestionService questionService;
	private final AnswerService answerService;
	
	
	@ApiOperation(value = "로그인", notes = "로그인")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping("/user")
	@ResponseStatus(value = HttpStatus.OK)
	@Deprecated
	public ResponseEntity<User> login(
			@RequestBody UserReqDto userReqDto
			) {
		User user = userService.getUser(userReqDto);
		return new ResponseEntity<User>(user, HttpStatus.OK);
	}
	
	@RequiredToken
	@ApiOperation(value = "문제 목록 조회", notes = "문제 목록 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping("/questions")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<Question>> getQuestionList( 
			@RequestHeader(value = "user_id", required = true) Long userId ) {
		List<Question> list = questionService.getQuestionList(userId);
		return new ResponseEntity<List<Question>>(list, HttpStatus.OK);
	}
	
	@ApiOperation(value = "문제 상세 조회", notes = "문제 상세 조회")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping("/question/{question_id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<QuestionResDto> getQuestionList(
			@RequestHeader(value = "user_id", required = true ) Long userId,
			@PathVariable("question_id") Long questionId
			) {
		QuestionResDto questionResDto = questionService.getQuestion(userId, questionId);
		return new ResponseEntity<QuestionResDto>(questionResDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "답변 제출", notes = "답변 제출")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PostMapping("/answer")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<AnswerResDto> insertAnswer(
			@RequestHeader(value = "user_id", required = true) Long userId,
			@RequestBody @Valid AnswerReqDto answerReqDto, BindingResult bindingResult
			) {
		ErrorUtils.checkBindingResult(bindingResult);
		AnswerResDto answerResDto = answerService.insertAnswer(userId, answerReqDto);
		return new ResponseEntity<AnswerResDto>(answerResDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "최종 답변 제출", notes = "최종 답변 제출")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@PutMapping("/answer")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<AnswerSubmitResDto> updateAnswer(
			@RequestHeader(value = "user_id", required = true) Long userId,
			@RequestBody @Valid AnswerReqDto answerReqDto, BindingResult bindingResult
			) {
		ErrorUtils.checkBindingResult(bindingResult);
		AnswerSubmitResDto answerSubmitResDto = answerService.updateAnswer(userId, answerReqDto);
		return new ResponseEntity<AnswerSubmitResDto>(answerSubmitResDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "답변 제출 여부 확인", notes = "답변 제출 여부 확인")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping("/answer/check/{question_id}")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<AnswerSubmitResDto> submitAnswerCheck(
			@RequestHeader(value = "user_id", required = true) Long userId,
			@PathVariable("question_id") Long questionId
			) {
		AnswerSubmitResDto answerSubmitResDto = answerService.checkSubmitAnswer(userId, questionId);
		return new ResponseEntity<AnswerSubmitResDto>(answerSubmitResDto, HttpStatus.OK);
	}
	
	@ApiOperation(value = "답변 초기화", notes = "답변 완전 삭제")
	@ApiResponses(value = {
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@DeleteMapping("/answer/{question_id}")
	@ResponseStatus(value = HttpStatus.NO_CONTENT)
	public ResponseEntity<Void> deleteAnswer(
			@RequestHeader(value = "user_id", required = true) Long userId,
			@PathVariable("question_id") Long questionId
			) {
		answerService.deleteAnswerAndHistory(userId, questionId);
		return new ResponseEntity<Void>(HttpStatus.NO_CONTENT);
	}
	
	@ApiOperation(value = "현재 시간", notes = "현재 시간 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@GetMapping("/time")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<Long> getTime() {
		return new ResponseEntity<Long>(new Date().getTime(), HttpStatus.OK);
	}
	
	@ApiOperation(value = "현재 프로파일", notes = "프로파일 리턴 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "OK"),
			@ApiResponse(code = 400, message = "Bad Request"),
			@ApiResponse(code = 401, message = "Unauthorized"),
			@ApiResponse(code = 403, message = "Forbidden"),
			@ApiResponse(code = 404, message = "Not Found"),
			@ApiResponse(code = 500, message = "Internal Server Error") })
	@RequestMapping(value = "/profile", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public String getProfile(){
		return Arrays.stream(env.getActiveProfiles())
				.findFirst()
				.orElse("");
	}
}

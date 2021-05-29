package com.flex.api.controller;

import java.util.Arrays;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flex.api.model.Result;
import com.flex.api.service.CodingTestService;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@RestController
@RequestMapping("/flex")
@Api(tags = "Test", description = "Test Service APIs", consumes = "application/json", produces = "application/json")
public class CodingTestController {

	private Environment env;

	@Autowired
	CodingTestService service;

	@ApiOperation(value = "테스트", notes = "테스트 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "조회 성공"), //
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"), //
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"), //
			@ApiResponse(code = 500, message = "시스템 장애") })
	@RequestMapping(value = "/codingTest", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<List<Result>> getScoreCode(@RequestBody String code) {
		List<Result> rtn = service.getScoreCode(code);
		return new ResponseEntity<List<Result>>(rtn, HttpStatus.OK);
	}
	
	@ApiOperation(value = "현재 시간", notes = "현재 시간 api")
	@ApiResponses(value = { //
			@ApiResponse(code = 200, message = "조회 성공"), //
			@ApiResponse(code = 400, message = "올바르지 않은 입력값 존재"), //
			@ApiResponse(code = 404, message = "정보가 존재하지 않음"), //
			@ApiResponse(code = 500, message = "시스템 장애") })
	@RequestMapping(value = "/time", method = RequestMethod.GET)
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity getTime() {
		List<String> profiles = Arrays.asList(env.getActiveProfiles());

		return new ResponseEntity<>(Arrays.stream(env.getActiveProfiles())
				.findFirst()
				.orElse(""), HttpStatus.OK);

//		return new ResponseEntity<Long>(new Date().getTime(), HttpStatus.OK);
	}

}

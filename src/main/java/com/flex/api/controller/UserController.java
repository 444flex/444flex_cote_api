package com.flex.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.UserResDto;
import com.flex.api.service.UserService;

import io.swagger.annotations.ApiOperation;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flex/user")
public class UserController {

	private final UserService userService;
	
	
	@ApiOperation(value = "토큰 발급", notes = "토큰 발급")
	@PostMapping("/token")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<UserResDto> createAuthToken(@RequestBody UserReqDto userReqDto) {
		UserResDto userResDto = userService.createToken(userReqDto);
		return new ResponseEntity<UserResDto>(userResDto, HttpStatus.OK);
	}
}

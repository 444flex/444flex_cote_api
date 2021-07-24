package com.flex.api.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.flex.api.dto.response.UserResDto;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flex/user")
public class UserController {

	@PostMapping("/token")
	@ResponseStatus(value = HttpStatus.OK)
	public ResponseEntity<UserResDto> createAuthToken() {
		
		return null;
	}
}

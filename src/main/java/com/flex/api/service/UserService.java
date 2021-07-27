package com.flex.api.service;

import com.flex.api.dto.request.UserReqDto;
import com.flex.api.dto.response.UserResDto;
import com.flex.api.model.User;

public interface UserService {

	public User getUser(Long id);

	public User getUser(UserReqDto userReqDto);
	
	public UserResDto createToken(UserReqDto userReqDto);
}

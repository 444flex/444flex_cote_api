package com.flex.api.service.impl;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import com.flex.api.data.UserRepository;
import com.flex.api.dto.request.UserReqDto;
import com.flex.api.exception.EntityNotFoundException;
import com.flex.api.model.User;
import com.flex.api.service.UserService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	
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
}

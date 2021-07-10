package com.flex.api.data;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
	
	public boolean existsByNameAndCellNumber(String name, String cellNumber);
	
	public User findByNameAndCellNumber(String name, String cellNumber);

}

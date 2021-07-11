package com.flex.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.Parameter;

@Repository
public interface ParameterRepository extends JpaRepository<Parameter, Long>{

	public List<Parameter> findByQuestionId(Long questionId);
}

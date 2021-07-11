package com.flex.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.VerificationParam;

@Repository
public interface VerificationParamRepository extends JpaRepository<VerificationParam, Long>{

	public VerificationParam findByVerificationIdAndParameterId(Long verificationId, Long parameterId);
	
}

package com.flex.api.data;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.flex.api.model.Verification;

@Repository
public interface VerificationRepository extends JpaRepository<Verification, Long>{

	public List<Verification> findByQuestionId(Long questionId);
}

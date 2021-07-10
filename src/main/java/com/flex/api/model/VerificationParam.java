package com.flex.api.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@Entity
@Table(name = "verification_param")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class VerificationParam extends IdGenerationBaseEntity {

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "verification_id")
	@JsonIgnore
	private Verification verification;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "parameter_id")
	@JsonIgnore
	private Parameter parameter;
	
	@Column(name = "value")
	@NotNull
	private String value;
}

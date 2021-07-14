package com.flex.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@Entity
@Table(name = "parameter")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Parameter extends IdGenerationBaseEntity {
	
	public enum Type2 {
		single, multi
	}
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	@JsonIgnore
	private Question question;
	
	@Column(name = "type")
	@NotNull
	private String type;
	
	@Column(name = "type2")
	@Enumerated(EnumType.STRING)
	@NotNull
	private Type2 type2;
	
	@Column(name = "name")
	@NotNull
	private String name;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "parameter_id")
	@JsonIgnore
	private List<VerificationParam> verificationParamList;
	
}

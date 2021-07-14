package com.flex.api.model;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
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
@Table(name = "question")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class Question extends IdGenerationBaseEntity {
	
	public enum ReturnType2 {
		single, multi
	}

	@Column(name = "title")
	@NotNull
	private String title;
	
	@Column(name = "content")
	private String content;
	
	@Column(name = "method_name")
	@NotNull
	private String methodName;
	
	@Column(name = "return_type")
	@NotNull
	private String returnType;
	
	@Column(name = "return_type2")
	@Enumerated(EnumType.STRING)
	@NotNull
	private ReturnType2 returnType2;
	
	@Column(name = "limit_time")
	@NotNull
	private Integer limitTime;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	@JsonIgnore
	private List<Parameter> parameterList;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	@JsonIgnore
	private List<Verification> verificationList;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	@JsonIgnore
	private List<QuestionImage> questionImageList;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "question_id")
	@JsonIgnore
	private List<Answer> answerList;
}

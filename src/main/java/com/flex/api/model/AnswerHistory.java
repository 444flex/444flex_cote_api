package com.flex.api.model;

import java.util.List;

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
@Table(name = "answer_history")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class AnswerHistory extends IdGenerationBaseEntity {

	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "answer_id")
	@JsonIgnore
	private Answer answer;
	
	@Column(name = "score")
	@NotNull
	private Integer score;
	
	@Column(name = "compile_time")
	@NotNull
	private Long compileTime;
	
	@Column(name = "compile_yn")
	@NotNull
	private boolean compileYn;
	
	@Column(name = "file_name")
	private String fileName;
	
	@Column(name = "code")
	private String code;
}

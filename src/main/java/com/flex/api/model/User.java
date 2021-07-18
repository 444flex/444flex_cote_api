package com.flex.api.model;

import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

@Data
@Entity
@Table(name = "user")
@JsonInclude(Include.NON_NULL)
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
public class User extends IdGenerationBaseEntity {
	

	@Column(name = "name")
	@NotNull
	@ApiModelProperty(notes = "이름", required = true, example = "전지형")
	private String name;
	
	@Column(name = "cell_number")
	@NotNull
	@ApiModelProperty(notes = "휴대폰번호", required = true, example = "01063371590")
	private String cellNumber;
	
	@Column(name = "email")
	@ApiModelProperty(notes = "이메일", required = false, example = "naoog@naver.com")
	private String email;
	
	@Column(name = "company")
	@ApiModelProperty(notes = "소속", required = false, example = "롯데")
	private String company;
	
	@OneToMany(fetch = FetchType.LAZY)
	@JoinColumn(name = "user_id")
	@JsonIgnore
	private List<Answer> answerList;
	
	@Column(name = "first_login_time", insertable = false, updatable = true)
	private LocalDateTime firstLoginTime;
	
	public void setFirstLoginTime() {
		this.firstLoginTime = LocalDateTime.now();
	}
}

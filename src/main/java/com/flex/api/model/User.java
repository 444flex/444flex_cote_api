package com.flex.api.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonFormat;
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
public class User implements Serializable {
	
	public enum Role {
		GUEST, DEV
	}

	@Id
	@Column(name = "id")
	@ApiModelProperty(notes = "id", required = true)
	private Long id;
	
	@Column(name = "insert_time", insertable = false, updatable = false)
	@ApiModelProperty(notes = "입력 시간 (millisecond)", dataType = "java.lang.Long", allowEmptyValue = true, required = false, example = "1565571041000")
	@JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "s")
	private Date insertTime;
	
	@Column(name = "name")
	@NotNull
	@ApiModelProperty(notes = "이름", required = true, example = "전지형")
	private String name;
	
	@Column(name = "cell_number")
	@NotNull
	@ApiModelProperty(notes = "휴대폰번호", required = true, example = "01063371590")
	private String cellNumber;
	
	@Column(name = "role")
	@Enumerated(EnumType.STRING)
	@ApiModelProperty(notes = "사용자 role")
	private Role role;
}

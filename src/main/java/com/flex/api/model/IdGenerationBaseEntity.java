package com.flex.api.model;

import java.util.Date;

import javax.persistence.Column;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@MappedSuperclass
public abstract class IdGenerationBaseEntity extends BaseEntity {
	
	@Id
	@Column(name = "id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	protected Long id;
	
	@Column(name = "insert_time", insertable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "s")
	protected Date insertTime;

	@Column(name = "update_time", insertable = false, updatable = false)
	@JsonFormat(shape = JsonFormat.Shape.NUMBER, pattern = "s")
	protected Date updateTime;
	
	@Column(name = "delete_time")
	@JsonIgnore
	protected Date deleteTime;
	
	@JsonIgnore
	public void setToDeleted() {
		this.deleteTime = new Date(System.currentTimeMillis());
	}
	
	@JsonIgnore
	public boolean isEnabled() {
		if(this.deleteTime != null) return false;
		return true;
	}
}

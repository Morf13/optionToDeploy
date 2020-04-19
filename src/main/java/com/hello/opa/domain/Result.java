package com.hello.opa.domain;

import java.sql.Date;
import java.time.LocalDateTime;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

@Entity
@Table(name = "results")
public class Result {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Long id;
	private Long userId;
	
	private Long exerciseId;
	private int score;  
	private String exerciseTitle;
	private String username;
	private String modtime;
	
	public Result() {
		
	}
	public Result(Long userId, Long exerciseId, int score, String modtime, String exerciseTitle, String username) {
		this.userId = userId;
		this.exerciseId = exerciseId;
		this.score = score;
		this.modtime = modtime;
		this.exerciseTitle = exerciseTitle;
		this.username = username;
	}
	public Long getUserId() {
		return userId;
	}
	public void setUserId(Long user_id) {
		this.userId = user_id;
	}
	public Long getExerciseId() {
		return exerciseId;
	}
	public void setExerciseId(Long exerciseId) {
		this.exerciseId = exerciseId;
	}
	public int getScore() {
		return score;
	}
	public void setScore(int score) {
		this.score = score;
	}
	public String getModtime() {
		return modtime;
	}
	public void setModtime(String modtime) {
		this.modtime = modtime;
	}
	public String getExerciseTitle() {
		return exerciseTitle;
	}
	public void setExerciseTitle(String exerciseTitle) {
		this.exerciseTitle = exerciseTitle;
	}
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	

}

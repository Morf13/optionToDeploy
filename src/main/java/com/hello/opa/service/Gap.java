package com.hello.opa.service;

public class Gap {
	private int id;
	private String taskLeft;
	private String taskRight;
	public Gap(int id, String taskLeft, String answer,String taskRight) {
		this.id = id;
		this.taskLeft = taskLeft;
		this.taskRight = taskRight;
		this.answer = answer;
	}
	private String answer;
	
	public String getTaskLeft() {
		return taskLeft;
	}
	public void setTaskLeft(String taskLeft) {
		this.taskLeft = taskLeft;
	}
	public String getTaskRight() {
		return taskRight;
	}
	public void setTaskRight(String taskRight) {
		this.taskRight = taskRight;
	}
	public String getAnswer() {
		return answer;
	}
	public void setAnswer(String answer) {
		this.answer = answer;
	}
	public int getId() {
		return id;
	}
	
	public boolean check(String pupilAnswer) {
		if(this.answer.equals(pupilAnswer)) return true;
		return false;
		
	}

}

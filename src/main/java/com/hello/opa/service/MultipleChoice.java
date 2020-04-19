package com.hello.opa.service;

import java.util.ArrayList;
import java.util.Collections;

public class  MultipleChoice{
	private int id;
	private String task;
	private ArrayList<String> answers = new ArrayList<String>();
	private ArrayList<String> rightAnswers = new ArrayList<String>();;

	public MultipleChoice(Integer id, String task) {
		this.task = task;
	}

	public int getId() {
		return id;
	}

	public void setAnswers(String answer) {
		this.answers.add(answer);
		Collections.shuffle(this.answers);
	}

	public void setRightAnswers(String answer) {
		this.rightAnswers.add(answer);
	}

	public String getTask() {
		return task;
	}

	public ArrayList<String> getAnswers() {
		return answers;
	}

	public ArrayList<String> getRightAnswers() {
		return rightAnswers;
	}

	public double check(ArrayList<String> pupilsAnswers) {
		double score = 0;
		for (String pupilsAnswer : pupilsAnswers) {
			if (this.rightAnswers.contains(pupilsAnswer))
				score += 100 / rightAnswers.size();
			if (this.rightAnswers.size() < pupilsAnswers.size())
				score = 0;
			if (!this.rightAnswers.contains(pupilsAnswer))
				score = 0;
		}
		return score;

	}

}

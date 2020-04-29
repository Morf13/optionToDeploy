package com.hello.opa.service;

import com.hello.opa.domain.Exercise;
import com.hello.opa.domain.User;
//import com.hello.opa.domain.dto.ExerciseDto;
import com.hello.opa.repos.ExerciseRepository;

import antlr.collections.List;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Stack;

import org.apache.poi.hssf.usermodel.HSSFCellStyle;
import org.apache.poi.hssf.usermodel.HSSFFont;
import org.apache.poi.hssf.util.HSSFColor;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.ss.usermodel.Workbook;
import org.apache.poi.xssf.usermodel.XSSFCellStyle;
import org.apache.poi.xssf.usermodel.XSSFColor;
import org.apache.poi.xssf.usermodel.XSSFFont;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ExerciseService {
	@Autowired
	private ExerciseRepository exerciseRepository;

	public Page<Exercise> exerciseList(Pageable pageable) {

		return exerciseRepository.findAll(pageable);

	}

	public Page<Exercise> exerciseListForUser(Pageable pageable, User currentUser, User author) {
		// TODO Auto-generated method stub
		return exerciseRepository.findByUser(pageable, author);
	}

	public ArrayList<MultipleChoice> getMultipleChoiceText(Long id) {

		ArrayList<MultipleChoice> exerciseForView = new ArrayList<>();
		Exercise exercise = exerciseRepository.findById(id).get();
		String[] sentences = exercise.getTask().split("&");

		for (int i = 0; i < sentences.length; i++) {
			String[] m = sentences[i].split("@");
			MultipleChoice multipleChoice = new MultipleChoice(i, m[0] + " ... " + m[3]);
			String[] answer = m[1].split(",");
			String[] rightAnswer = m[2].split(",");
			for (int k = 0; k < answer.length; k++) {
				multipleChoice.setAnswers(answer[k]);
			}
			for (int k = 0; k < rightAnswer.length; k++) {
				multipleChoice.setRightAnswers(rightAnswer[k]);
			}
			exerciseForView.add(multipleChoice);
		}

		return exerciseForView;

	}

	public Stack<String> checkMultipleChoice(Map<String, String> formAnswers, ArrayList<MultipleChoice> data) {
		Map<String, ArrayList<String>> studentAnswers = new HashMap<>();
		double totalScore = 0;
		Stack<String> mistakes = new Stack<String>();
		for (int i = 1; i <= data.size(); i++)
			studentAnswers.put(i + "", new ArrayList<String>());

		formAnswers.forEach((k, v) -> {
			if (!k.equals("_csrf"))
				studentAnswers.get(k.substring(0, k.indexOf("/"))).add(v);
		});

		for (int i = 0; i < data.size(); i++) {

			ArrayList<String> answers = studentAnswers.get(i + 1 + "");
			if (answers != null) {
				totalScore += data.get(i).check(answers);
				mistakes.push(data.get(i).getTask() + " ANSWERS -> " + studentAnswers.get(i + 1 + "").toString());
			}
		}
		mistakes.push(totalScore / data.size() + "");
		return mistakes;

	}

	public ArrayList<Gap> getGap(Map<Integer, ArrayList<MyCell>> data) {
		ArrayList<Gap> exerciseForView = new ArrayList<>();

		for (int k = 0; k < data.size(); k++) {
			Gap gap = new Gap(k + 1, data.get(k).get(0).getContent(), data.get(k).get(1).getContent(),
					data.get(k).get(2).getContent());

			exerciseForView.add(gap);
		}
		return exerciseForView;
	}

	public ArrayList<Gap> getGapText(Long id) {
		ArrayList<Gap> exerciseForView = new ArrayList<>();
		Exercise exercise = exerciseRepository.findById(id).get();
		String[] sentences = exercise.getTask().split("&");
		for (int i = 0; i < sentences.length; i++) {
			String[] m = sentences[i].split("@");
			Gap gap = new Gap(i, m[0], m[1], m[2]);
			exerciseForView.add(gap);
		}

		return exerciseForView;

	}

	public Stack<String> checkGap(Map<String, String> formAnswers, ArrayList<Gap> data) {
		double totalScore = 0;
		Stack<String> mistakes = new Stack<String>();
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).check(formAnswers.get(i + 1 + "")))
				totalScore += 100 / data.size();
			else {
				mistakes.add(data.get(i).getTaskLeft() + " ... " + data.get(i).getTaskRight() + " MISTAKE -> "
						+ formAnswers.get(i + 1 + ""));
			}
		}
		mistakes.push(totalScore + "");
		return mistakes;
	}

}
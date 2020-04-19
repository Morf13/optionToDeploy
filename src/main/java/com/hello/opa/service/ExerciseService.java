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

	@Value("${upload.path}")
	private String uploadPath;

	public Page<Exercise> exerciseList(Pageable pageable) {

		return exerciseRepository.findAll(pageable);

	}

	public Page<Exercise> exerciseListForUser(Pageable pageable, User currentUser, User author) {
		// TODO Auto-generated method stub
		return exerciseRepository.findByUser(pageable, author);
	}

	public Map<Integer, ArrayList<MyCell>> getExercise(Long id) throws IOException {

		Exercise exercise = exerciseRepository.findById(id).get();
		FileInputStream file = new FileInputStream(new File(""));// new File(uploadPath + "/" +
																	// exercise.getFileName()));
		Workbook workbook = new XSSFWorkbook(file);
		Sheet sheet = workbook.getSheetAt(0);

		Map<Integer, ArrayList<MyCell>> data = new HashMap<>();
		int i = 0;
		for (Row row : sheet) {
			data.put(i, new ArrayList<MyCell>());
			for (Cell cell : row) {
				XSSFCellStyle cellStyle = (XSSFCellStyle) cell.getCellStyle();

				MyCell myCell = new MyCell(cell.getRichStringCellValue().getString());
				XSSFColor bgColor = cellStyle.getFillForegroundColorColor();
				if (bgColor != null) {
					byte[] rgbColor = bgColor.getRGB();
					myCell.setBgColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0]) + ","
							+ (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + ","
							+ (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")");
				}
				XSSFFont font = cellStyle.getFont();
				myCell.setTextSize(font.getFontHeightInPoints() + "");
				if (font.getBold()) {
					myCell.setTextWeight("bold");
				}
				XSSFColor textColor = font.getXSSFColor();
				if (textColor != null) {
					byte[] rgbColor = textColor.getRGB();
					myCell.setTextColor("rgb(" + (rgbColor[0] < 0 ? (rgbColor[0] + 0xff) : rgbColor[0]) + ","
							+ (rgbColor[1] < 0 ? (rgbColor[1] + 0xff) : rgbColor[1]) + ","
							+ (rgbColor[2] < 0 ? (rgbColor[2] + 0xff) : rgbColor[2]) + ")");
				}
				data.get(i).add(myCell);
			}
			i++;
		}

		return data;

	}

	public ArrayList<MultipleChoice> getMultipleChoice(Map<Integer, ArrayList<MyCell>> data) {

		ArrayList<MultipleChoice> exerciseForView = new ArrayList<>();

		for (int k = 0; k < data.size(); k++) {
			MultipleChoice multipleChoice = new MultipleChoice(k + 1, data.get(k).get(0).getContent());
			for (int j = 1; j < data.get(k).size(); j++) {
				if (data.get(k).get(j).getTextWeight().equals("bold"))
					multipleChoice.setRightAnswers(data.get(k).get(j).getContent());
				multipleChoice.setAnswers(data.get(k).get(j).getContent());
			}
			exerciseForView.add(multipleChoice);
		}
		return exerciseForView;

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

	public double checkMultipleChoice(Map<String, String> formAnswers, ArrayList<MultipleChoice> data) {
		Map<String, ArrayList<String>> studentAnswers = new HashMap<>();
		double totalScore = 0;
		for (int i = 1; i <= data.size(); i++)
			studentAnswers.put(i + "", new ArrayList<String>());
		formAnswers.forEach((k, v) -> {
			if (!k.equals("_csrf"))
				studentAnswers.get(k.substring(0, k.indexOf("/"))).add(v);
		});

		for (int i = 0; i < data.size(); i++) {

			ArrayList<String> answers = studentAnswers.get(i + 1 + "");
			if (answers != null)
				totalScore += data.get(i).check(answers);
		}
		return totalScore / data.size();

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

	public double checkGap(Map<String, String> formAnswers, ArrayList<Gap> data) {
		double totalScore = 0;
		for (int i = 0; i < data.size(); i++) {
			if (data.get(i).check(formAnswers.get(i + 1 + "")))
				totalScore += 100 / data.size();
		}
		return totalScore;
	}

}
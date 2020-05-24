package com.hello.opa.controller;

import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Stack;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hello.opa.domain.Exercise;
import com.hello.opa.domain.Result;
import com.hello.opa.domain.User;
import com.hello.opa.repos.ExerciseRepository;
import com.hello.opa.repos.ResultRepository;
import com.hello.opa.service.ExerciseService;
import com.hello.opa.service.Gap;
import com.hello.opa.service.MailSender;
import com.hello.opa.service.MultipleChoice;

@Controller
public class ExerciseController {

	@Autowired
	private ExerciseRepository exerciseRepository;

	@Autowired
	private ResultRepository resultRepo;
	
	@Autowired
	private MailSender mailSender;

	@Autowired
	ExerciseService exerciseService;

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(Model model, @PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Exercise> page = exerciseService.exerciseList(pageable);

		model.addAttribute("page", page);
		model.addAttribute("url", "/main");

		return "main";
	}


	@GetMapping("/addExerciseText")
	public String addExText(Model model) {

		return "addExerciseText";
	}

	@PostMapping("/addExerciseText")
	public String addExerciseText(@AuthenticationPrincipal User user, @Valid Exercise exercise,
			BindingResult bindingResult, Model model, @RequestParam Map<String, String> form) {
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errorMap);
			model.addAttribute("exercise", exercise);
			return "addExerciseText";
		} else {
			exercise.setAuthor(user);
			exercise.setType(form.get("type"));
			exercise.setTask(form.get("task"));
			exercise.setExplanation(form.get("explanation"));
			exercise.setTopic(form.get("topic"));
			exerciseRepository.save(exercise);
		}

		return "redirect:/user-exercises/" + user.getId();
	}
	
	@GetMapping("/deleteExercise/{exercise}")
	public String deleteExercise(Model model, @AuthenticationPrincipal User user, @Valid Exercise exercise) {
		exerciseRepository.delete(exercise);

		return "redirect:/user-exercises/" + user.getId();
	}

	@GetMapping("/editExerciseText/{exercise}")
	public String editExText(Model model, @AuthenticationPrincipal User user, @Valid Exercise exercise) {
		model.addAttribute("exercise", exercise);

		return "editExerciseText";
	}

	@PostMapping("/editExerciseText/{exercise}")
	public String editSaveExText(Model model, @AuthenticationPrincipal User user, @Valid Exercise exercise,
			@RequestParam Map<String, String> form, BindingResult bindingResult) {
		
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errorMap);
			model.addAttribute("exercise", exercise);
			return "editExerciseText";
		} else {
			exercise.setAuthor(user);
			exercise.setTask(form.get("task"));
			exercise.setExplanation(form.get("explanation"));
			exercise.setTopic(form.get("topic"));
			exerciseRepository.save(exercise);
		}
		
		return "editExerciseText";
	}



	@GetMapping("/user-exercises/{user}")
	public String userExercises(@AuthenticationPrincipal User currentUser, @PathVariable User user, Model model,
			@RequestParam(required = false) Exercise exercise,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {
		Page<Exercise> page = exerciseService.exerciseListForUser(pageable, currentUser, user);

		model.addAttribute("page", page);
		model.addAttribute("exercise", exercise);
		model.addAttribute("isCurrentUser", currentUser.equals(user));
		model.addAttribute("url", "/user-exercises/" + user.getId());

		return "userExercises";
	}


	@GetMapping("/exercise/mchoice/{exercise}")
	public String multipleChoice(@PathVariable Exercise exercise, Model model) throws IOException {
		ArrayList<MultipleChoice> data = exerciseService.getMultipleChoiceText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("exerciseExpl", exercise.getExplanation());
		model.addAttribute("size", data.size());

		return "multiple";
	}

	@PostMapping("/exercise/mchoice/{exercise}")
	public String checkMultipleChoice(@AuthenticationPrincipal User currentUser, @PathVariable Exercise exercise,
			Model model, @RequestParam Map<String, String> form) throws IOException {

		ArrayList<MultipleChoice> data = exerciseService.getMultipleChoiceText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("exerciseExpl", exercise.getExplanation());
		model.addAttribute("size", data.size());
		Stack<String> res = exerciseService.checkMultipleChoice(form, data);
		double result =  Double.parseDouble(res.pop());
		model.addAttribute("result", result);
		
		if (!currentUser.isTeacher() && !currentUser.isAdmin()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = LocalDateTime.now().format(formatter);
			Result results = new Result(currentUser.getId(), exercise.getId(), (int) result, now, exercise.getTitle(),
					currentUser.getUsername());
			mailSender.send("marininfor@yandex.ru", "results from "+ currentUser.getUsername() + " " + exercise.getTitle(), res.toString());
			resultRepo.save(results);
		}
		return "multiple";

	}

	@GetMapping("/exercise/gap/{exercise}")
	public String gap(@PathVariable Exercise exercise, Model model) throws IOException {
		ArrayList<Gap> data = exerciseService.getGapText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("exerciseExpl", exercise.getExplanation());
		model.addAttribute("size", data.size());

		return "gap";
	}

	@PostMapping("/exercise/gap/{exercise}")
	public String checkGap(@AuthenticationPrincipal User currentUser,@PathVariable Exercise exercise, Model model, @RequestParam Map<String, String> form) {
		ArrayList<Gap> data = exerciseService.getGapText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("exerciseExpl", exercise.getExplanation());
		model.addAttribute("size", data.size());
		Stack<String> res =  exerciseService.checkGap(form, data);
		double result = Double.parseDouble(res.pop());
		model.addAttribute("result", result);
		
		if (!currentUser.isTeacher() && !currentUser.isAdmin()) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = LocalDateTime.now().format(formatter);
			Result results = new Result(currentUser.getId(), exercise.getId(), (int) result, now, exercise.getTitle(),
					currentUser.getUsername());
			mailSender.send("marininfor@yandex.ru", "results from "+ currentUser.getUsername() + " " + exercise.getTitle(), res.toString());
			resultRepo.save(results);
		}
		return "gap";
	}

}

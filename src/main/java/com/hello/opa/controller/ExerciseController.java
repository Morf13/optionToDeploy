package com.hello.opa.controller;

import java.io.File;
import java.io.IOException;
import java.sql.Date;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import com.hello.opa.domain.Exercise;
import com.hello.opa.domain.Result;
import com.hello.opa.domain.User;
import com.hello.opa.repos.ExerciseRepository;
import com.hello.opa.repos.ResultRepository;
import com.hello.opa.service.ExerciseService;
import com.hello.opa.service.Gap;
import com.hello.opa.service.MultipleChoice;
import com.hello.opa.service.MyCell;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

@Controller
public class ExerciseController {

	@Autowired
	private ExerciseRepository exerciseRepository;

	@Value("${upload.path}")
	private String uploadPath;
	@Autowired
	private ResultRepository resultRepo;

	@Autowired
	ExerciseService exerciseService;

	@GetMapping("/")
	public String greeting(Map<String, Object> model) {
		return "greeting";
	}

	@GetMapping("/main")
	public String main(Model model,
			@PageableDefault(sort = { "id" }, direction = Sort.Direction.DESC) Pageable pageable) {

		Page<Exercise> page = exerciseService.exerciseList(pageable);

		model.addAttribute("page", page);
		model.addAttribute("url", "/main");

		return "main";
	}

	@GetMapping("/addExercise")
	public String addEx(Model model) {

		return "addExercise";
	}
	@GetMapping("/addExerciseText")
	public String addExText(Model model) {

		return "addExerciseText";
	}
	
	@PostMapping("/addExerciseText")
	public String addExerciseText(@AuthenticationPrincipal User user, @Valid Exercise exercise, BindingResult bindingResult,
			Model model,  @RequestParam Map<String, String> form

	)  {
		exercise.setAuthor(user);
		exercise.setType(form.get("type"));
		exercise.setTask(form.get("task"));
		

			exerciseRepository.save(exercise);
		

		return "redirect:/user-exercises/" + user.getId();
//		model.addAttribute("check", form);
//		return "check";
	}
	
	@GetMapping("/editExerciseText/{exercise}")
	public String editExText(Model model, @AuthenticationPrincipal User user,@PathVariable Exercise exercise) { 
		model.addAttribute("exercise",exercise );

		return "editExerciseText";
	}
	
	@PostMapping("/editExerciseText/{exercise}")
	public String editSaveExText(Model model, @AuthenticationPrincipal User user,@PathVariable Exercise exercise, @RequestParam Map<String, String> form) { 
		model.addAttribute("exercise",exercise );
		exercise.setTitle(form.get("title"));
		exercise.setTask(form.get("task"));
		exerciseRepository.save(exercise);
		return "editExerciseText";
	}

	@PostMapping("/addExercise")
	public String add(@AuthenticationPrincipal User user, @Valid Exercise exercise, BindingResult bindingResult,
			Model model, @RequestParam("file") MultipartFile file, @RequestParam Map<String, String> form

	) throws IOException {
		exercise.setAuthor(user);
		exercise.setType(form.get("type"));
		if (bindingResult.hasErrors()) {
			Map<String, String> errorMap = ControllerUtils.getErrors(bindingResult);
			model.mergeAttributes(errorMap);
			model.addAttribute("exercise", exercise);
			return "addExercise";
		} else {
			saveFile(exercise, file);
			model.addAttribute("exercise", null);

			exerciseRepository.save(exercise);
		}

		return "redirect:/user-exercises/" + user.getId();
//		model.addAttribute("check", form);
//		return "check";
	}

	private void saveFile(@Valid Exercise exercise, @RequestParam("file") MultipartFile file) throws IOException {
		if (file != null && !file.getOriginalFilename().isEmpty()) {
			File uploadDir = new File(uploadPath);

			if (!uploadDir.exists()) {
				uploadDir.mkdir();
			}

			String uuidFile = UUID.randomUUID().toString();
			String resultFilename = uuidFile + "." + file.getOriginalFilename();

			file.transferTo(new File(uploadPath + "/" + resultFilename));

			//exercise.setFileName(resultFilename);
		}
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

	@PostMapping("/user-exercises/{user}")
	public String updateExercise(@AuthenticationPrincipal User currentUser, @PathVariable Long user,
			@RequestParam("id") Exercise exercise, @RequestParam("title") String title,
			@RequestParam("file") MultipartFile file) throws IOException {
		if (exercise.getAuthor().equals(currentUser)) {
			if (!StringUtils.isEmpty(title)) {
				exercise.setTitle(title);
			}

			saveFile(exercise, file);

			exerciseRepository.save(exercise);
		}

		return "redirect:/user-exercises/" + user;
	}

	@GetMapping("/exercise/mchoice/{exercise}")
	public String multipleChoice(@PathVariable Exercise exercise, Model model) throws IOException {
		ArrayList<MultipleChoice> data = exerciseService
				.getMultipleChoiceText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("size", data.size());

		return "multiple";
	}

	@PostMapping("/exercise/mchoice/{exercise}")
	public String checkMultipleChoice(@AuthenticationPrincipal User currentUser, @PathVariable Exercise exercise,
			Model model, @RequestParam Map<String, String> form) throws IOException {

		ArrayList<MultipleChoice> data = exerciseService
				.getMultipleChoiceText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("size", data.size());
		double result = exerciseService.checkMultipleChoice(form, data);
		model.addAttribute("result", result);
		if (!currentUser.isTeacher()&&!currentUser.isAdmin() ) {
			DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
			String now = LocalDateTime.now().format(formatter);
			Result results = new Result(currentUser.getId(), exercise.getId(), (int) result, now,exercise.getTitle(),currentUser.getUsername());
			resultRepo.save(results);
		}
		return "multiple";

	}

	@GetMapping("/exercise/gap/{exercise}")
	public String gap(@PathVariable Exercise exercise, Model model) throws IOException {
		ArrayList<Gap> data = exerciseService.getGapText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("size", data.size());

		return "gap";
	}

	@PostMapping("/exercise/gap/{exercise}")
	public String checkGap(@PathVariable Exercise exercise, Model model, @RequestParam Map<String, String> form)
			throws IOException {

		ArrayList<Gap> data = exerciseService.getGapText(exercise.getId());
		model.addAttribute("exercise", data);
		model.addAttribute("exerciseTitle", exercise.getTitle());
		model.addAttribute("size", data.size());
		double result = exerciseService.checkGap(form, data);
		model.addAttribute("result", result);
		return "gap";
//		
//		model.addAttribute("check", form);
//		return "check";

	}

}

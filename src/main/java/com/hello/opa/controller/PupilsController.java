package com.hello.opa.controller;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.hello.opa.domain.Exercise;
import com.hello.opa.domain.Result;
import com.hello.opa.domain.Role;
import com.hello.opa.domain.User;
import com.hello.opa.repos.ExerciseRepository;
import com.hello.opa.repos.ResultRepository;
import com.hello.opa.repos.UserRepository;
import com.hello.opa.service.ExerciseService;

@Controller
public class PupilsController {
	
	@Autowired
    private UserRepository userRepo;
	@Autowired
    private ResultRepository resultRepo;
	
	
	
	@Autowired
    private ExerciseRepository exRepo;
	@Autowired
	ExerciseService exerciseService;
	
	 @GetMapping("/teacher/add-pupils/{user}")
	    public String addPupil(Model model, @PathVariable User user) {
		 
	    	 model.addAttribute("users", userRepo.findAllByRoles(Role.USER));

	        return "addPupil";
	    }
	 @PostMapping("/teacher/add-pupils/{user}")
		public String checkMultipleChoice(@PathVariable User user, Model model, @RequestParam Map<String, String> form)
				 {
		 
		 User teacher = userRepo.findById(Long.parseLong(form.get("teacher")));
		 HashSet<User> teachers = new HashSet<User>();
		 teachers.add(teacher);
		 HashSet<User> pupils = new HashSet<User>();
		 	for(String k: form.keySet()) {
		 		
		 		if(!k.equals("_csrf")&&!k.equals("teacher")) {
		 			User pupil = userRepo.findById(Long.parseLong(k));
		 			 
		 			 pupils.add(pupil);
		 			
		 			teacher.setPupils(pupils);
		 			pupil.setTeachers(teachers);
		 		}
		 	}
		
			return "addPupil";
			
		}
	 
	 	@GetMapping("/teacher-pupils/{user}")
	    public String myPupils(@PathVariable User user, Model model) {
	 		 
	    	 model.addAttribute("pupils", user.getPupils());
	    	 model.addAttribute("exercises", user.getExercises());

	        return "myPupils";
	    }
	 	
	 	@PostMapping("/teacher-pupils/{user}")
	    public String addExercisesToPupils(Model model, @PathVariable User user, @RequestParam Map<String, String> form) {
	 		HashSet<Exercise> exercises = new HashSet<Exercise>();
	 		HashSet<User> pupils = new HashSet<User>(); 
	 		for(String k: form.keySet()) {
		 		
		 		if(!k.equals("_csrf")&&!k.equals("teacher")) {
		 			if(k.startsWith("exercise")) {exercises.add(exRepo.findById(Long.parseLong(k.substring(8))));}
		 			if(k.startsWith("pupil")) {pupils.add(userRepo.findById(Long.parseLong(k.substring(5))));}
		 			
		 		}
		 	}
	 		for(User u: pupils) {
	 			u.setExercisesTodo(exercises);
	 		}
	 		
	 		

	        return "myPupils";
	    }
	 	
	 	@GetMapping("/pupil/exercises/{user}")
	    public String myExercisesPupil(@PathVariable User user, Model model) {

			model.addAttribute("exercises", user.getExercisesTodo());
			

	        return "myExercisesPupil";
	    }
	 	@GetMapping("/pupil/results/{user}")
	    public String myResults(@PathVariable User user, Model model) {

			model.addAttribute("results", resultRepo.findAllByUser_id(user.getId()));
			

	        return "myResults";
	    }
	 	@GetMapping("/teacher/results/{user}")
	    public String myPupilsResults(@PathVariable User user, Model model, Pageable pageable) {
	 		Set<User> pupilSet = user.getPupils();
	 		ArrayList<Result> results = new ArrayList<Result>();
	 		for(User u: pupilSet) {
	 			for(Result r:resultRepo.findAllByUser_id(u.getId())) results.add(r);
	 		}
	 		int start = (int) pageable.getOffset();
	 		int end = (start + pageable.getPageSize()) > results.size() ? results.size() : (start + pageable.getPageSize());
	 		Page<Result> page = new PageImpl<Result>(results.subList(start, end), pageable, results.size());
	 		model.addAttribute("page", page);
			model.addAttribute("url", "/teacher/results/"+ user.getId());
			model.addAttribute("results", results);
			

	        return "myPupilsResults";
	    }

}

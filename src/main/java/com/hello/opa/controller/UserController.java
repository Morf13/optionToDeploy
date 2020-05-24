package com.hello.opa.controller;


import com.hello.opa.domain.Role;
import com.hello.opa.domain.User;
import com.hello.opa.domain.dto.CaptchaResponseDto;
import com.hello.opa.repos.UserRepository;
import com.hello.opa.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.Valid;

@Controller
@RequestMapping("/user")
@PreAuthorize("hasAuthority('ADMIN')")
public class UserController {
    @Autowired
    private UserRepository userRepo;
    @Autowired
    private UserService userSevice;

    @GetMapping
    public String userList(Model model) {
        model.addAttribute("users", userRepo.findAll());

        return "userList";
    }
    @GetMapping("/add")
    public String addUser(Model model) {

        return "addUser";
    }
    @PostMapping("/add")
    public String addUser(
    		@RequestParam("password2") String passwordConfirm,
    		@Valid User user, 
    		BindingResult bindingResult, 
    		Model model) {
    	boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
    	if(isConfirmEmpty) {
    		
    		model.addAttribute("password2Error", "Enter password confirmation");
    	}
    	if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }
    	
    	 if (isConfirmEmpty || bindingResult.hasErrors()) {
             Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

             model.mergeAttributes(errors);

             return "addUser";
         }
        if (!userSevice.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "addUser";
        }

        return "addUser";
    }

    @GetMapping("{user}")
    public String userEditForm(@PathVariable User user, Model model) {
        model.addAttribute("user", user);
        model.addAttribute("roles", Role.values());

        return "userEdit";
    }

    @PostMapping
    public String userSave(
            @RequestParam String username,
            @RequestParam Map<String, String> form,
            @RequestParam("userId") User user
    ) {
        user.setUsername(username);

        Set<String> roles = Arrays.stream(Role.values())
                .map(Role::name)
                .collect(Collectors.toSet());

        user.getRoles().clear();

        for (String key : form.keySet()) {
            if (roles.contains(key)) {
                user.getRoles().add(Role.valueOf(key));
            }
        }

        userRepo.save(user);

        return "redirect:/user";
    }
    
   
}
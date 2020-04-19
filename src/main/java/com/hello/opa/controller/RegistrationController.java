package com.hello.opa.controller;


import com.hello.opa.domain.User;
import com.hello.opa.domain.dto.CaptchaResponseDto;
import com.hello.opa.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Map;

import javax.validation.Valid;

@Controller
public class RegistrationController {
	
	private final static String CAPTCHA_URL = "https://www.google.com/recaptcha/api/siteverify?secret=%s&response=%s";
    @Autowired
    private UserService userSevice;
    @Autowired
    private RestTemplate restTemplate;
    @Value("${recaptcha.secret}")
    private String secret;

    @GetMapping("/registration")
    public String registration() {
        return "registration";
    }

    @PostMapping("/registration")
    public String addUser(
    		@RequestParam("password2") String passwordConfirm,
    		@RequestParam("g-recaptcha-response") String captchaResponse,
    		@Valid User user, 
    		BindingResult bindingResult, 
    		Model model) {
    	String url = String.format(CAPTCHA_URL,secret, captchaResponse);
    	CaptchaResponseDto response = restTemplate.postForObject(url, Collections.emptyList(), CaptchaResponseDto.class);
    	if(!response.isSuccess()) {
    		model.addAttribute("captchaError", "Fill captcha");
    	}
    	boolean isConfirmEmpty = StringUtils.isEmpty(passwordConfirm);
    	if(isConfirmEmpty) {
    		
    		model.addAttribute("password2Error", "Enter password confirmation");
    	}
    	if (user.getPassword() != null && !user.getPassword().equals(passwordConfirm)) {
            model.addAttribute("passwordError", "Passwords are different!");
        }
    	
    	 if (isConfirmEmpty || bindingResult.hasErrors()||!response.isSuccess()) {
             Map<String, String> errors = ControllerUtils.getErrors(bindingResult);

             model.mergeAttributes(errors);

             return "registration";
         }

        if (!userSevice.addUser(user)) {
            model.addAttribute("usernameError", "User exists!");
            return "registration";
        }

        return "redirect:/login";
    }

    @GetMapping("/activate/{code}")
    public String activate(Model model, @PathVariable String code) {
        boolean isActivated = userSevice.activateUser(code);

        if (isActivated) {
        	model.addAttribute("messageType", "success");
            model.addAttribute("message", "User successfully activated");
        } else {
        	model.addAttribute("messageType", "danger");
            model.addAttribute("message", "Activation code is not found!");
        }

        return "login";
    }
}
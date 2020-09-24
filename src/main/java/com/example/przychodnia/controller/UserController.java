package com.example.przychodnia.controller;

import java.util.Collections;
import java.util.HashMap;

import javax.validation.Valid;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.przychodnia.entity.MyUserDetails;
import com.example.przychodnia.entity.Role;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.service.MessageService;
import com.example.przychodnia.service.RoleService;
import com.example.przychodnia.service.UserService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final RoleService roleService;
    private final MessageService messageService;

    @GetMapping("/register")
    public String registerForm(Model model) {
        model.addAttribute("user", new User());
        return "user/register";
    }
    
    @GetMapping("/user")
    public String index() {
    	return "user/user";
    }
    
    @GetMapping("/user/messages")
    public String messages(Model model, @AuthenticationPrincipal MyUserDetails currentUser) {
    	model.addAttribute("messages", messageService.findAllByUserId(currentUser.getUser().getId()));
    	return "user/messages";
    }

    @PostMapping("/register")
    public String register(@Valid User user, BindingResult bindingResult, Model model) {

        boolean error = false;
        HashMap<String, String> errors = new HashMap<>();

        if (userService.findByUserName(user.getUserName()).isPresent()) {
            errors.put("userName", "użytkownik o takim loginie już istnieje");
            error = true;
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            errors.put("email", "użytkownik o takim adresie email już istnieje");
            error = true;
        }
        if (userService.findByPersonalIdentityNumber(user.getPersonalIdentityNumber()).isPresent()) {
            errors.put("personalIdentityNumber", "użytkownik o takim numerze PESEL już istnieje");
            error = true;
        }
        if (bindingResult.hasErrors()) {
            error = true;
        }

        if (error) {
            model.addAttribute("uniqueErrors", errors);
            return "user/register";
        }
        Role role = roleService.findByRoleName("ROLE_PATIENT").orElse(null);
        user.setRoles(Collections.singletonList(role));
        userService.save(user);
        return "redirect:/";
    }

}

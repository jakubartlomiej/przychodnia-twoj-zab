package com.example.przychodnia.controller;

import com.example.przychodnia.entity.MyUserDetails;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import com.example.przychodnia.entity.Message;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.MessageService;

import lombok.RequiredArgsConstructor;

@Controller
@RequiredArgsConstructor
public class HomePageController {

    private final ContactDataService contactDataService;
    private final MessageService messageService;

    @GetMapping("/")
    public String index(Model model, @AuthenticationPrincipal MyUserDetails currentUser) {
        model.addAttribute("contactData", contactDataService.findAll().get(0));
        Message message = new Message();
        if(currentUser != null) {
        	message.setName(currentUser.getUser().getFirstName() + " " + currentUser.getUser().getLastName());        	
        	message.setEmail(currentUser.getUser().getEmail());        	
        	message.setNumber(currentUser.getUser().getPhone());      
        }
		model.addAttribute(message);
        return "index";
    }

    @PostMapping("/sendMessage")
    public String sendMessage(Message message, @AuthenticationPrincipal MyUserDetails currentUser) {
    	message.setUser(currentUser.getUser());
        messageService.save(message);
        return "redirect:/";
    }
}

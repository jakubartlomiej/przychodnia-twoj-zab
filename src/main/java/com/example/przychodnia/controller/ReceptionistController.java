package com.example.przychodnia.controller;

import com.example.przychodnia.entity.Message;
import com.example.przychodnia.entity.MyUserDetails;
import com.example.przychodnia.entity.VisitsCalendar;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.MessageService;
import com.example.przychodnia.service.UserService;
import com.example.przychodnia.service.VisitsCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;

@Controller
@RequestMapping("/receptionist")
@RequiredArgsConstructor
public class ReceptionistController {

    private final MessageService messageService;
    private final UserService userService;
    private final ContactDataService contactDataService;
    private final VisitsCalendarService visitsCalendarService;

    private LocalDateTime getDateFromInputDate(String inputDate) {
        String[] inputDateTab = inputDate.split("-");
        return LocalDateTime.of(Integer.parseInt(inputDateTab[0]), Integer.parseInt(inputDateTab[1]), Integer.parseInt(inputDateTab[2]), 0, 0);
    }

    @GetMapping("")
    public String receptionist(Model model) {
        model.addAttribute("newMessages", messageService.newMessagesCount());
        return "receptionist/receptionist";
    }

    @GetMapping("/messages")
    public String messages(Model model) {
        model.addAttribute("newMessages", messageService.newMessagesCount());
        model.addAttribute("messages", messageService.findAll());
        return "receptionist/messages/messages";
    }

    @GetMapping("/message/{id}")
    public String message(Model model, @PathVariable String id) {
        Message message = messageService.findById(id);
        message.setOpened(true);
        messageService.save(message);
        model.addAttribute("newMessages", messageService.newMessagesCount());
        model.addAttribute("message", message);
        return "receptionist/messages/message";
    }

    @GetMapping("/message/{id}/delete")
    public String delete(@PathVariable String id) {
        messageService.deleteById(id);
        return "redirect:/receptionist/messages";
    }

    @GetMapping("/visits")
    public String getFormVisits(Model model){
        model.addAttribute("dentistList", userService.findAllDoctor());
        return "receptionist/visits/form";
    }

    @GetMapping("/visit/register/{date}/{id}")
    public String getRegisterWeekByDate(@PathVariable String id, @PathVariable String date, Model model) {
        LocalTime open = contactDataService.findAll().get(0).getOpen();
        int defoultTimeVisit = 30;
        model.addAttribute("doctor", userService.findById(id));
        model.addAttribute("dentistList", userService.findAllDoctor());
        model.addAttribute("visitsCalendar", visitsCalendarService.findByDoctorId(id));
        model.addAttribute("today", getDateFromInputDate(date));
        model.addAttribute("dayOfWeek", getDateFromInputDate(date).getDayOfWeek().getValue());
        model.addAttribute("difrence", defoultTimeVisit);
        model.addAttribute("open", open);
        return "receptionist/visits/calendar";
    }

    @PostMapping("/visit/register")
    public String postRegisterWeek(Model model, @RequestParam String date, @RequestParam String dentist){
        LocalTime open = contactDataService.findAll().get(0).getOpen();
        int defoultTimeVisit = 30;
        model.addAttribute("doctor", userService.findById(dentist));
        model.addAttribute("dentistList", userService.findAllDoctor());
        model.addAttribute("visitsCalendar", visitsCalendarService.findByDoctorId(dentist));
        model.addAttribute("today", getDateFromInputDate(date));
        model.addAttribute("dayOfWeek", getDateFromInputDate(date).getDayOfWeek().getValue());
        model.addAttribute("difrence", defoultTimeVisit);
        model.addAttribute("open", open);
        return "receptionist/visits/calendar";
    }

    @GetMapping("/visit/register/add/{id}")
    public String addVisit(@PathVariable String id,Model model){
        model.addAttribute("patientList",userService.findAllPatient());
        model.addAttribute(visitsCalendarService.findById(Long.parseLong(id)));
        return "receptionist/visits/add";
    }

    @PostMapping("/visit/register/add/{id}")
    public String updateVisitCalendarPatientId(@PathVariable String id, @RequestParam String patient){
        VisitsCalendar visit = visitsCalendarService.findById(Long.parseLong(id));
        visit.setPatient(userService.findById(patient));
        visitsCalendarService.addVisit(visit);
        return "receptionist/visits/added";
    }

    @GetMapping("/visit/register/update/{id}")
    public String updateVisit(@PathVariable String id,Model model){
        model.addAttribute("patientList",userService.findAllPatient());
        model.addAttribute(visitsCalendarService.findById(Long.parseLong(id)));
        return "receptionist/visits/update";
    }

    @PostMapping("/visit/register/update/{id}")
    public String deletePatientIdVisitCalendar(@PathVariable String id){
        VisitsCalendar visit = visitsCalendarService.findById(Long.parseLong(id));
        visit.setPatient(null);
        visitsCalendarService.addVisit(visit);
        return "receptionist/visits/delete";
    }
}

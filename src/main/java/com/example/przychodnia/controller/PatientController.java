package com.example.przychodnia.controller;

import com.example.przychodnia.entity.MyUserDetails;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.entity.VisitsCalendar;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.UserService;
import com.example.przychodnia.service.VisitsCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/patient")
public class PatientController {

    private final VisitsCalendarService visitsCalendarService;
    private final UserService userService;
    private final ContactDataService contactDataService;

    private LocalDateTime getDateFromInputDate(String inputDate) {
        String[] inputDateTab = inputDate.split("-");
        return LocalDateTime.of(Integer.parseInt(inputDateTab[0]), Integer.parseInt(inputDateTab[1]), Integer.parseInt(inputDateTab[2]), 0, 0);
    }

    @GetMapping("")
    public String patient() {
        return "patient/patient";
    }

    @GetMapping("/visit/incoming")
    public String visits(@AuthenticationPrincipal MyUserDetails currentUser, Model model){
        Optional<User> user2 =  userService.findByUsername(currentUser.getUsername());
        String id_user = user2.get().getId().toString();
        model.addAttribute("visits",visitsCalendarService.findByPatientId(id_user));
        return "patient/visit/incoming";
    }

    @GetMapping("/visit/delete/{visitId}")
    public String takeVisit(@PathVariable String visitId) {
        VisitsCalendar visitsCalendar = visitsCalendarService.findById(Long.parseLong(visitId));
        visitsCalendar.setPatient(null);
        visitsCalendarService.addVisit(visitsCalendar);
        return "patient/visit/delete";
    }

    @GetMapping("/visit/register")
    public String getFormViewCalendar(Model model){
        model.addAttribute("dentistList", userService.findAllDoctor());
        return "patient/register/form";
    }

    @GetMapping("/visit/register/{date}/{dentist}")
    public String getRegisterWeekByDate(Model model, @PathVariable String date, @PathVariable String dentist){
        LocalTime open = contactDataService.findAll().get(0).getOpen();
        int defoultTimeVisit = 30;
        model.addAttribute("doctor", userService.findById(dentist));
        model.addAttribute("dentistList", userService.findAllDoctor());
        model.addAttribute("visitsCalendar", visitsCalendarService.findByDoctorId(dentist));
        model.addAttribute("today", getDateFromInputDate(date));
        model.addAttribute("dayOfWeek", getDateFromInputDate(date).getDayOfWeek().getValue());
        model.addAttribute("difrence", defoultTimeVisit);
        model.addAttribute("open", open);
        return "patient/register/calendar";
    }

    @PostMapping("/visit/register/week")
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
        return "patient/register/calendar";
    }

    @GetMapping("/visit/register/add/{id}")
    public String addVisit(@PathVariable String id,Model model){
        model.addAttribute(visitsCalendarService.findById(Long.parseLong(id)));
        return "patient/register/add";
    }

    @PostMapping("/visit/register/add/{id}")
    public String updateVisitCalendarPatientId(@PathVariable String id, @AuthenticationPrincipal MyUserDetails currentUser){
        VisitsCalendar visit = visitsCalendarService.findById(Long.parseLong(id));
        visit.setPatient(currentUser.getUser());
        visitsCalendarService.addVisit(visit);
        return "patient/register/added";
    }
}

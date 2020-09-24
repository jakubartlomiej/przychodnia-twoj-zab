package com.example.przychodnia.controller;

import com.example.przychodnia.entity.MyUserDetails;
import com.example.przychodnia.entity.Schedule;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.ScheduleService;
import com.example.przychodnia.service.UserService;
import com.example.przychodnia.service.VisitsCalendarService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.web.bind.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/dentist")
@RequiredArgsConstructor
public class DentistController {

    private final UserService userService;
    private final VisitsCalendarService visitsCalendarService;
    private final ContactDataService contactDataService;
    private final ScheduleService scheduleService;

    private String getCurrentUser(@AuthenticationPrincipal MyUserDetails currentUser) {
        Optional<User> user = userService.findByUserName(currentUser.getUsername());
        return user.get().getId().toString();
    }

    private LocalDateTime getDateFromInputDate(String inputDate) {
        String[] inputDateTab = inputDate.split("-");
        return LocalDateTime.of(Integer.parseInt(inputDateTab[0]), Integer.parseInt(inputDateTab[1]), Integer.parseInt(inputDateTab[2]), 0, 0);
    }


    private void dataToScheduleWeek(@AuthenticationPrincipal MyUserDetails currentUser, @PathVariable String date, Model model, String s) {
        model.addAttribute("daysSchedule", scheduleService.findWeekByDay(date, Long.parseLong(getCurrentUser(currentUser))));
        model.addAttribute("today", getDateFromInputDate(date));
        model.addAttribute("dayOfWeek", getDateFromInputDate(date).getDayOfWeek().getValue());
        model.addAttribute("freeDayInWeek", scheduleService.findFreeDayInWeek(getDateFromInputDate(s), Long.parseLong(getCurrentUser(currentUser))));
    }

    @GetMapping("")
    public String dentist() {
        return "dentist/dentist";
    }

    @GetMapping("/calendar")
    public String getCalendar(Model model) {
        String inputDateString = LocalDate.now().toString();
        model.addAttribute("inputDateString", inputDateString);
        return "dentist/calendar/form";
    }

    @PostMapping("/calendar/week")
    public String postCalendarByWeek(@AuthenticationPrincipal MyUserDetails currentUser, @RequestParam String inputDateString, Model model) {
        int defoultTimeVisit = 30;
        model.addAttribute("visits", visitsCalendarService.findByDoctorId(getCurrentUser(currentUser)));
        model.addAttribute("today", getDateFromInputDate(inputDateString));
        model.addAttribute("dayOfWeek", getDateFromInputDate(inputDateString).getDayOfWeek().getValue());
        model.addAttribute("open", contactDataService.findAll().get(0).getOpen());
        model.addAttribute("difrence", defoultTimeVisit);
        model.addAttribute(inputDateString);
        return "dentist/calendar/calendar";
    }

    @GetMapping("/schedule")
    public String getSchedule(Model model) {
        String inputDateString = LocalDate.now().toString();
        model.addAttribute("inputDateString", inputDateString);
        return "dentist/schedule/form";
    }

    @PostMapping("/schedule/week")
    public String postScheduleWeek(@AuthenticationPrincipal MyUserDetails currentUser, @RequestParam String inputDateString, Model model) {
        dataToScheduleWeek(currentUser, inputDateString, model, inputDateString);
        model.addAttribute(inputDateString);
        return "dentist/schedule/schedule";
    }

    @GetMapping("/schedule/week/{date}")
    public String getScheduleWeekByDate(@AuthenticationPrincipal MyUserDetails currentUser, @PathVariable String date, Model model) {
        dataToScheduleWeek(currentUser, date, model, date.toString());
        return "dentist/schedule/schedule";
    }

    @GetMapping("/schedule/add/{date}")
    public String addScheduleDayForm(@PathVariable String date, @AuthenticationPrincipal MyUserDetails currentUser, Model model) {
        model.addAttribute("inputDateString", date);
        model.addAttribute("doctorId", getCurrentUser(currentUser));
        model.addAttribute(new Schedule());
        return "dentist/schedule/add";
    }

    @PostMapping("/schedule/add/{date}")
    public String addScheduleDay(@PathVariable String date, Schedule schedule) {
        scheduleService.addSchedule(schedule);
        return "redirect:/dentist/schedule/week/" + date;
    }

    @GetMapping("/schedule/update/{id}")
    public String updateScheduleForm(@PathVariable String id, Model model) {
        model.addAttribute("scheduleById", scheduleService.findById(id));
        model.addAttribute("visitsInDay", visitsCalendarService.findByDoctorAndDateCount(scheduleService.findById(id).get().getDoctor().getId().toString(), scheduleService.findById(id).get().getDate().toString()));
        return "dentist/schedule/update";
    }

    @PostMapping("schedule/delete/{id}")
    public String updateSchedule(@PathVariable String id,@RequestParam String date,@AuthenticationPrincipal MyUserDetails currentUser) {
        visitsCalendarService.deleteByDateAndDoctor(date,Long.parseLong(getCurrentUser(currentUser)));
        scheduleService.deleteScheduleById(id);
        return "redirect:/dentist/schedule/week/" + date;
    }
}

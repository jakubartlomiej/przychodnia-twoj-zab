package com.example.przychodnia.controller;

import com.example.przychodnia.entity.ContactData;
import com.example.przychodnia.entity.Role;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.RoleService;
import com.example.przychodnia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;
import java.util.HashMap;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final ContactDataService contactDataService;
    private final UserService userService;
    private final RoleService roleService;

    @GetMapping("")
    public String admin() {
        return "admin/admin";
    }

    @GetMapping("/contactData/update")
    public String updateContactDataForm(Model model) {
        model.addAttribute(contactDataService.findAll().get(0));
        return "admin/contactData/update";
    }

    @PostMapping("/contactData/update/{id}")
    public String updateContactData(ContactData contactData, @PathVariable String id) {
        contactDataService.save(contactData);
        return "redirect:/admin/contactData/update";
    }

    @GetMapping("/user/{id}")
    public String user(@PathVariable String id, Model model) {
        model.addAttribute("user", userService.findById(id));
        return "admin/user/user";
    }

    @GetMapping("/user/add")
    public String addUserForm(Model model) {
        model.addAttribute(new User());
        return "admin/user/add";
    }

    @PostMapping("/user/add")
    public String addUser(@Valid User user, BindingResult bindingResult, Model model) {
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
            return "admin/user/add";
        }
        userService.save(user);
        return "redirect:/admin/user/list";
    }

    @GetMapping("/user/{id}/update")
    public String updateUserForm(Model model, @PathVariable String id) {
        model.addAttribute(userService.findById(id));
        return "admin/user/update";
    }

    @PostMapping("/user/{id}/update")
    public String updateUser(@Valid User user, BindingResult bindingResult, Model model, @PathVariable String id) {
        boolean error = false;
        HashMap<String, String> errors = new HashMap<>();
        User oldUser = userService.findById(id);

        if (userService.findByUserName(user.getUserName()).isPresent()) {
            if (!oldUser.getUserName().equals(user.getUserName())) {
                errors.put("userName", "użytkownik o takim loginie już istnieje");
                error = true;
            }
        }
        if (userService.findByEmail(user.getEmail()).isPresent()) {
            if (!oldUser.getEmail().equals(user.getEmail())) {
                errors.put("email", "użytkownik o takim adresie email już istnieje");
                error = true;
            }
        }
        if (userService.findByPersonalIdentityNumber(user.getPersonalIdentityNumber()).isPresent()) {
            if (!oldUser.getPersonalIdentityNumber().equals(user.getPersonalIdentityNumber())) {
                errors.put("personalIdentityNumber", "użytkownik o takim numerze PESEL już istnieje");
                error = true;
            }
        }
        if (bindingResult.hasErrors()) {
            error = true;
        }

        if (error) {
            model.addAttribute("uniqueErrors", errors);
            return "admin/user/update";
        }
        userService.update(user);
        return "redirect:/admin/user/" + id;
    }

    @GetMapping("/user/list")
    public String list(Model model) {
        model.addAttribute("users", userService.findAll());
        return "admin/user/list";
    }

    @GetMapping("/user/{id}/delete")
    public String delete(@PathVariable String id) {
        userService.deleteById(id);
        return "redirect:/admin/user/list";
    }

    @GetMapping("/user/{id}/role/grant")
    public String grantForm(Model model, @PathVariable String id) {
        model.addAttribute("roles", roleService.findAll(id));
        model.addAttribute("role", new Role());
        return "admin/user/role/grant";
    }

    @PostMapping("/user/{id}/role/grant")
    public String grant(@PathVariable String id, Role role) {
        userService.addRole(role, id);
        return "redirect:/admin/user/" + id;
    }

    @GetMapping("/user/{id}/role/delete")
    public String takeForm(Model model, @PathVariable String id) {
        model.addAttribute("roles", userService.findById(id).getRoles());
        model.addAttribute("role", new Role());
        return "admin/user/role/delete";
    }

    @PostMapping("/user/{id}/role/delete")
    public String take(@PathVariable String id, Role role) {
        userService.deleteRole(role, id);
        return "redirect:/admin/user/" + id;
    }
}

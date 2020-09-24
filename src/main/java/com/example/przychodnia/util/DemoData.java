package com.example.przychodnia.util;

import com.example.przychodnia.entity.ContactData;
import com.example.przychodnia.entity.Role;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.service.ContactDataService;
import com.example.przychodnia.service.RoleService;
import com.example.przychodnia.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DemoData implements CommandLineRunner {

    private final ContactDataService contactDataService;
    private final RoleService roleService;
    private final UserService userService;

    @Override
    public void run(String... args) {
        if (contactDataService.count() == 0) {
            contactDataService.save(new ContactData());
        }

        if (roleService.count() == 0) {
            roleService.save(new Role("ROLE_ADMIN", "Administrator"));
            roleService.save(new Role("ROLE_DENTIST", "Dentysta"));
            roleService.save(new Role("ROLE_ASSISTANT", "Asystent"));
            roleService.save(new Role("ROLE_RECEPTIONIST", "Recepcjonista"));
            roleService.save(new Role("ROLE_PATIENT", "Pacjent"));
        }

        if (userService.count() == 0) {
            Optional<Role> role_admin = roleService.findByRoleName("ROLE_ADMIN");

            User user = new User();
            user.setUserName("admin");
            user.setPassword("123");
            user.setRoles(new ArrayList<>());
            user.getRoles().add(role_admin.orElse(null));

            userService.save(user);
        }
    }
}
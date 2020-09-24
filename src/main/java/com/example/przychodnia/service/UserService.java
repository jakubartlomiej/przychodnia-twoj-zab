package com.example.przychodnia.service;

import java.util.*;
import java.util.stream.Collectors;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.przychodnia.entity.Role;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.repository.RoleRepository;
import com.example.przychodnia.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    public void update(User user) {
        userRepository.save(user);
    }

    public long count() {
        return userRepository.count();
    }

    public List<User> findAll() {
        return userRepository.findAll();
    }

    public Optional<User> findByUserName(String userName) {
        return userRepository.findByUserName(userName);
    }

    public Optional<User> findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public Optional<User> findByPersonalIdentityNumber(String personalIdentityNumber) {
        return userRepository.findByPersonalIdentityNumber(personalIdentityNumber);
    }

    public void deleteById(String id) {
        userRepository.deleteById(Long.parseLong(id));
    }

    public void addRole(Role role, String userId) {
        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
        role = roleRepository.findById(role.getId()).orElse(null);

        if (user != null) {
            if (role != null) {
                user.getRoles().add(role);
                userRepository.save(user);
            }
        }
    }

    public User findById(String id) {
        return userRepository.findById(Long.parseLong(id)).orElse(null);
    }

    public void deleteRole(Role role, String userId) {
        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
        role = roleRepository.findById(role.getId()).orElse(null);

        if (user != null) {
            if (role != null) {
                user.getRoles().remove(role);
                userRepository.save(user);
            }
        }
    }

    public Optional<User> findByUsername(String username) {
        return userRepository.findByUserName(username);
    }

    public List<User> findAllDoctor() {
        List<User> doctorList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            for (Role role : user.getRoles()) {
                if (role.getRoleName().equals("ROLE_DENTIST")) {
                    doctorList.add(user);
                }
            }
        }
        return doctorList.stream()
                .sorted(Comparator.comparing(User::getLastName))
                .collect(Collectors.toList());
    }

    public List<User> findAllPatient() {
        List<User> patientList = new ArrayList<>();
        for (User user : userRepository.findAll()) {
            for (Role role : user.getRoles()) {
                if (role.getRoleName().equals("ROLE_PATIENT")) {
                    patientList.add(user);
                }
            }
        }
        return patientList.stream()
                .sorted(Comparator.comparing(User::getLastName))
                .collect(Collectors.toList());
    }
}

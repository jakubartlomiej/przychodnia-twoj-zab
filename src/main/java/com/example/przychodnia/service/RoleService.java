package com.example.przychodnia.service;

import com.example.przychodnia.entity.Role;
import com.example.przychodnia.entity.User;
import com.example.przychodnia.repository.RoleRepository;
import com.example.przychodnia.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoleService {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;


    public void save(Role role) {
        roleRepository.save(role);
    }

    public long count() {
        return roleRepository.count();
    }

    public Optional<Role> findByRoleName(String roleName) {
        return roleRepository.findByRoleName(roleName);
    }

    public List<Role> findAll(String userId) {
        User user = userRepository.findById(Long.parseLong(userId)).orElse(null);
        List<Role> roles = roleRepository.findAll();
        if (user != null) {
            for (Role role : user.getRoles()) {
                roles.remove(role);
            }
        }
        return roles;
    }
}

package com.example.UserAuth.security;

import com.example.UserAuth.model.Role;
import com.example.UserAuth.model.User;
import com.example.UserAuth.model.UserRole;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Configuration
public class RoleInitializationConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${role.admin}")
    private String adminRoleName;

    @Value("${role.user}")
    private String userRoleName;

    @PostConstruct
    @Transactional
    public void init() {
        // Initialize roles
        Role adminRole = roleRepository.findByName(adminRoleName);
        Role userRole = roleRepository.findByName(userRoleName);

        if (adminRole == null) {
            adminRole = new Role();
            adminRole.setName(adminRoleName);
            roleRepository.save(adminRole);
        }

        if (userRole == null) {
            userRole = new Role();
            userRole.setName(userRoleName);
            roleRepository.save(userRole);
        }
    }
}

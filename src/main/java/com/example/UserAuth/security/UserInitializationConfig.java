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
import org.springframework.context.annotation.DependsOn;
import org.springframework.context.annotation.Profile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

import jakarta.annotation.PostConstruct;

@Configuration
@Profile({"test", "local"})
@DependsOn("roleInitializationConfig")
public class UserInitializationConfig {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Value("${admin.username:}")
    private String adminUsername;

    @Value("${admin.password:}")
    private String adminPassword;

    @Value("${user.username:}")
    private String userUsername;

    @Value("${user.password:}")
    private String userPassword;

    @Value("${role.admin}")
    private String adminRoleName;

    @Value("${role.user}")
    private String userRoleName;

    @PostConstruct
    @Transactional
    public void init() {
        // Initialize roles (in case RoleInitializationConfig was not run)
        Role adminRole = roleRepository.findByName(adminRoleName);
        Role userRole = roleRepository.findByName(userRoleName);

        // Initialize an admin user if not already present
        if (userRepository.findByUsername(adminUsername) == null) {
            User adminUser = new User();
            adminUser.setUsername(adminUsername);
            adminUser.setPassword(passwordEncoder.encode(adminPassword));
            adminUser.setEnabled(true);
            userRepository.save(adminUser);

            UserRole adminUserRole = new UserRole();
            adminUserRole.setUser(adminUser);
            adminUserRole.setRole(adminRole);
            userRoleRepository.save(adminUserRole);
        }

        // Initialize a regular user if not already present
        if (userRepository.findByUsername(userUsername) == null) {
            User regularUser = new User();
            regularUser.setUsername(userUsername);
            regularUser.setPassword(passwordEncoder.encode(userPassword));
            regularUser.setEnabled(true);
            userRepository.save(regularUser);

            UserRole regularUserRole = new UserRole();
            regularUserRole.setUser(regularUser);
            regularUserRole.setRole(userRole);
            userRoleRepository.save(regularUserRole);
        }
    }
}

package com.example.UserAuth.service;

import com.example.UserAuth.exception.RoleNotFoundException;
import com.example.UserAuth.exception.UsernameAlreadyExistsException;
import com.example.UserAuth.model.User;
import com.example.UserAuth.model.Role;
import com.example.UserAuth.model.UserRole;
import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.repository.RoleRepository;
import com.example.UserAuth.repository.UserRoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.User.UserBuilder;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class UserService implements UserDetailsService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRoleRepository userRoleRepository;

    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    @Value("${role.user}")
    private String userRoleName;

    @Transactional
    public void save(User user) throws UsernameAlreadyExistsException, RoleNotFoundException {
        logger.info("Attempting to save user: {}", user.getUsername());
        if (userRepository.findByUsername(user.getUsername()) != null) {
            String errorMessage = String.format("Username already taken: %s", user.getUsername());
            logger.warn(errorMessage);
            throw new UsernameAlreadyExistsException(errorMessage);
        }

        Role roleUser = roleRepository.findByName(userRoleName);
        if (roleUser == null) {
            String errorMessage = String.format("Role not found: {}", userRoleName);
            logger.error(errorMessage);
            throw new RoleNotFoundException(errorMessage);
        }

        user.setPassword(bCryptPasswordEncoder.encode(user.getPassword()));
        user.setEnabled(true);
        userRepository.save(user);
        logger.info("User saved successfully: {}", user.getUsername());

        UserRole userRole = new UserRole();
        userRole.setUser(user);
        userRole.setRole(roleUser);
        userRoleRepository.save(userRole);
        logger.info("UserRole saved successfully: {} -> {}", user.getId(), roleUser);
    }

    public User findByUsername(String username) {
        logger.info("Finding user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user != null) {
            logger.info("User found: {}", username);
        } else {
            logger.warn("User not found: {}", username);
        }
        return user;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        logger.info("Loading user by username: {}", username);
        User user = userRepository.findByUsername(username);
        if (user == null) {
            logger.error("User not found: {}", username);
            throw new UsernameNotFoundException("User not found");
        }
        UserBuilder builder = org.springframework.security.core.userdetails.User.withUsername(user.getUsername());
        builder.password(user.getPassword());
        builder.roles("USER");
        logger.info("User loaded successfully: {}", username);
        return builder.build();
    }
}

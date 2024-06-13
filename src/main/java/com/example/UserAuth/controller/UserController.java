package com.example.UserAuth.controller;

import com.example.UserAuth.exception.UsernameAlreadyExistsException;
import com.example.UserAuth.model.User;
import com.example.UserAuth.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Controller
@RequestMapping
public class UserController {

    private static final Logger logger = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @GetMapping("/")
    public String home() {
        logger.info("Accessing home page");
        return "home";
    }

    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error, Model model, HttpServletRequest request) {
        logger.info("Accessing login page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            logger.info("User already authenticated, redirecting to dashboard");
            return "redirect:/dashboard";
        }
        if (error != null) {
            String errorMessage = (String) request.getSession().getAttribute("errorMessage");
            model.addAttribute("errorMessage", errorMessage);
            logger.warn("Login error: {}", errorMessage);
        }
        return "login"; // This returns the login.html template
    }

    @GetMapping("/register")
    public String showRegistrationForm(Model model) {
        logger.info("Accessing registration page");
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null && auth.isAuthenticated() && !(auth.getPrincipal() instanceof String)) {
            logger.info("User already authenticated, redirecting to dashboard");
            return "redirect:/dashboard";
        }
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(User userForm, Model model) {
        logger.info("Registering new user: {}", userForm.getUsername());
        try {
            userService.save(userForm);
            logger.info("User registered successfully: {}", userForm.getUsername());
            return "redirect:/login";
        } catch (UsernameAlreadyExistsException e) {
            logger.error("Registration error: Username already exists - {}", userForm.getUsername(), e);
            model.addAttribute("errorMessage", e.getMessage());
            model.addAttribute("user", userForm);
            return "register";
        }
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
        logger.info("Accessing dashboard for user: {}", username);
        model.addAttribute("username", username);
        return "dashboard";
    }
}

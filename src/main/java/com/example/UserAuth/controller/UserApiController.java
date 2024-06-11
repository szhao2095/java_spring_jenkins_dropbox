package com.example.UserAuth.controller;

import com.example.UserAuth.service.UserService;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@RestController
@RequestMapping(path = "api/vi/user")
public class UserApiController {
    @Autowired
    private UserService userService;

    @GetMapping
    public String test() {
        return "Test Successful";
    }

}

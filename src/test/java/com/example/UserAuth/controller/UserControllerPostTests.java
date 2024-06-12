//// TODO: What to test for in controller that isn't just going to be an integration test?
//
//package com.example.UserAuth.controller;
//
//import org.junit.jupiter.api.extension.ExtendWith;
//import org.mockito.junit.jupiter.MockitoExtension;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import com.example.UserAuth.model.User;
//import com.example.UserAuth.repository.UserRepository;
//import com.example.UserAuth.service.UserService;
//import org.junit.jupiter.api.BeforeEach;
//import org.junit.jupiter.api.Test;
//import org.mockito.InjectMocks;
//import org.mockito.Mock;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
//import org.springframework.security.test.context.support.WithAnonymousUser;
//import org.springframework.test.web.servlet.MockMvc;
//import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
//import static org.assertj.core.api.Assertions.assertThat;
//
//import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
//
//
//@ExtendWith(MockitoExtension.class)
//@SpringBootTest
//@AutoConfigureMockMvc
//public class UserControllerPostTests {
//
//    @Autowired
//    private MockMvc mockMvc;
//
//    @Autowired
//    private UserRepository userRepository;
//
//    @Autowired
//    private UserService userService;
//
//    @Autowired
//    private BCryptPasswordEncoder bCryptPasswordEncoder;
//
//    @BeforeEach
//    public void setup() {
//        // Clear the repository to avoid conflicts between tests
//        userRepository.deleteAll();
//
//        // Create a test user
//        User testUser = new User();
//        testUser.setUsername("testuser");
//        testUser.setPassword(bCryptPasswordEncoder.encode("password"));
//        userRepository.save(testUser);
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void UserController_Login_UnauthUserValidCredentialsRedirectToDashboard() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                        .param("username", "testuser")
//                        .param("password", "password"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void UserController_Login_UnauthUserInvalidCredentialsFail() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/login")
//                        .param("username", "testuser")
//                        .param("password", "wrongpassword"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login?error"));
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void UserController_Register_ExistingUsernameFail() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/register")
//                        .param("username", "testuser")
//                        .param("password", "newpassword"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("register"))
//                .andExpect(model().attributeExists("errorMessage"))
//                .andExpect(model().attributeExists("user"));
//
//        User existingUser = userRepository.findByUsername("testuser");
//        assertThat(existingUser).isNotNull();
//        assertThat(bCryptPasswordEncoder.matches("password", existingUser.getPassword())).isTrue();
//    }
//
//    @Test
//    @WithAnonymousUser
//    public void UserController_Register_NewUsernameSuccess() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/register")
//                        .param("username", "newuser")
//                        .param("password", "password"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//
//        User newUser = userRepository.findByUsername("newuser");
//        assertThat(newUser).isNotNull();
//        assertThat(bCryptPasswordEncoder.matches("password", newUser.getPassword())).isTrue();
//    }
//}
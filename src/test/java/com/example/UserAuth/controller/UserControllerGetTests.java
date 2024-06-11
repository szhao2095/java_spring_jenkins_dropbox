package com.example.UserAuth.controller;

import com.example.UserAuth.repository.UserRepository;
import com.example.UserAuth.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithAnonymousUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerGetTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserService userService;

    @Mock
    private Authentication authentication;

    @Mock
    private SecurityContext securityContext;

    @InjectMocks
    private UserController userController;

    @BeforeEach
    public void setup() {
        userRepository.deleteAll();
        SecurityContextHolder.setContext(securityContext);
    }

    @Test
    @WithAnonymousUser
    public void UserController_Home_AnyOneCanGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void UserController_Home_LoggedInUserCanAccess() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"));
    }

    @Test
    @WithAnonymousUser
    public void UserController_Login_AnyOneCanGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().isOk())
                .andExpect(view().name("login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void UserController_Login_LoggedInUserGetRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithAnonymousUser
    public void UserController_Register_AnyOneCanGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().isOk())
                .andExpect(view().name("register"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void UserController_Register_LoggedInUserGetRedirect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/register"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/dashboard"));
    }

    @Test
    @WithAnonymousUser
    public void UserController_Dashboard_AnyOneGetRediect() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("http://localhost/login"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    public void UserController_Dashboard_LoggedInUserCanGet() throws Exception {
        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard"))
                .andExpect(status().isOk())
                .andExpect(view().name("dashboard"));
    }

    // Functionality

//    @Test
//    public void UserController_Login_ValidCredentialsRedirectToDashboard() throws Exception {
//        Authentication authentication = mock(Authentication.class);
//        when(authentication.isAuthenticated()).thenReturn(true);
//        when(authentication.getPrincipal()).thenReturn(new Object()); // Mock a non-String principal
//
//        SecurityContext securityContext = mock(SecurityContext.class);
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        SecurityContextHolder.setContext(securityContext);
//
//        MockMvc mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/login"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/dashboard"));
//    }

//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void UserControllerTests_registerUser_savesUserAndRedirectsToLogin() throws Exception {
//        mockMvc.perform(MockMvcRequestBuilders.post("/register")
//                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
//                .param("username", "testuser")
//                .param("password", "password"))
//                .andExpect(status().is3xxRedirection())
//                .andExpect(redirectedUrl("/login"));
//
//        assertNotNull(userRepository.findByUsername("testuser"));
//    }
//
//    @Test
//    @WithMockUser(username = "user", roles = {"USER"})
//    public void UserControllerTests_dashboard_addsUsernameAndReturnsDashboard() throws Exception {
//        when(securityContext.getAuthentication()).thenReturn(authentication);
//        when(authentication.getName()).thenReturn("testuser");
//
//        mockMvc.perform(MockMvcRequestBuilders.get("/dashboard"))
//                .andExpect(status().isOk())
//                .andExpect(view().name("dashboard"))
//                .andExpect(model().attribute("username", "testuser"));
//    }
}
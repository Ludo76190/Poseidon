package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = LoginController.class)
class LoginControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void appLogin() throws Exception {
        mockMvc.perform(get("/app/login"))
                .andExpect(status().is(302))
                .andDo(print());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void appSecureArticleDetailAdmin() throws Exception {
        User user = new User();
        user.setId(1);
        user.setUsername("user");
        user.setPassword("password");
        user.setFullname("User Test");
        user.setRole("USER");
        List<User> users = new ArrayList<>();
        users.add(user);
        when(userService.getAllUser()).thenReturn(users);

        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(status().isOk())
                .andExpect(view().name("user/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void appSecureArticleDetailUser() throws Exception {

        mockMvc.perform(get("/app/secure/article-details"))
                .andExpect(status().is(403))
                .andExpect(forwardedUrl("/403"));
    }

    @Test
    public void appError() throws Exception {
        mockMvc.perform(get("/app/error"))
                .andExpect(status().is(200))
                .andExpect(view().name("403"))
                .andDo(print());
    }


}
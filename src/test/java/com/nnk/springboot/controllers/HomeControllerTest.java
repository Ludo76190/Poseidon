package com.nnk.springboot.controllers;

import com.nnk.springboot.services.UserDetailsServiceImpl;
import com.nnk.springboot.services.UserService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private UserService userService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void accessApp() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("home"))
                .andDo(print());
    }

    @Test
    public void adminHomeWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void adminHomeUser() throws Exception {
        mockMvc.perform(get("/admin/home"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrl("/bidList/list"))
                .andDo(print());
    }


}
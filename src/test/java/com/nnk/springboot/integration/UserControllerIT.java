package com.nnk.springboot.integration;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.security.test.context.support.WithUserDetails;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.model;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;


    User user = new User();
    User userAdmin = new User();
    User userUser = new User();

    @BeforeEach
    public void setUp() {
        user.setUsername("test");
        user.setFullname("Test");
        user.setRole("ADMIN3");
        user.setPassword("Az3rtyuio!");
        userRepository.save(user);

        userAdmin.setId(1);
        userAdmin.setUsername("test1");
        userAdmin.setFullname("Test1");
        userAdmin.setPassword("Test12345!");
        userAdmin.setRole("ADMIN");
        userRepository.save(userAdmin);

        userUser.setId(2);
        userUser.setUsername("test2");
        userUser.setFullname("Test2");
        userUser.setPassword("Test54321!");
        userUser.setRole("USER");
        userRepository.save(userUser);
    }

    @AfterEach
    public void clearAll() {
        this.userRepository.deleteAll();
        this.userRepository.deleteAll();

    }

    @Test
    public void getUserPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ADMIN"})
    public void getUserPageAdminTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test2", authorities = {"USER"})
    public void getUserPageUserTest() throws Exception {
        mockMvc.perform(get("/user/list"))
                .andExpect(status().is(403));

    }

    @WithMockUser(username = "test1", authorities = {"ADMIN"})
    @Test
    public void postUserValidateWithExistingUser() throws Exception {
        mockMvc.perform(post("/user/validate")
                        .param("username", "test3")
                        .param("fullname", "Test3")
                        .param("role", "ADMIN")
                        .param("password", "Az3rtyuio!")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/user/list"))
                .andExpect(model().hasNoErrors());
    }

}

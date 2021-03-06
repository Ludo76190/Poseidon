package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class RatingControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private RatingRepository ratingRepository;

    @Autowired
    private UserRepository userRepository;

    Rating rating = new Rating();
    User userAdmin = new User();
    User userUser = new User();

    @BeforeEach
    public void setUp() {
        rating.setMoodysRating("moodys");
        rating.setSandPRating("sand");
        rating.setFitchRating("fitch");
        ratingRepository.save(rating);

        userAdmin.setId(1);
        userAdmin.setUsername("test");
        userAdmin.setFullname("Test");
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
        this.ratingRepository.deleteAll();
        this.userRepository.deleteAll();

    }

    @Test
    public void getRatingPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/user/rating"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ADMIN"})
    public void getRatingPageAdminTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test2", authorities = {"USER"})
    public void getRatingPageUserTest() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk());

    }

}

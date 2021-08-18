package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RatingController.class)
class RatingControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RatingService ratingService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getRatingListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/rating/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingList() throws Exception {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("");
        rating.setSandPRating("");
        rating.setFitchRating("");
        rating.setOrderNumber(0);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        when(ratingService.getAllRating()).thenReturn(ratings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getRatingListAsUSER() throws Exception {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("");
        rating.setSandPRating("");
        rating.setFitchRating("");
        rating.setOrderNumber(0);
        List<Rating> ratings = new ArrayList<>();
        ratings.add(rating);
        when(ratingService.getAllRating()).thenReturn(ratings);

        mockMvc.perform(get("/rating/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("ratings", ratings))
                .andExpect(content().string(not(containsString("&nbsp;|&nbsp;<a href=\"/user/list\">User</a>"))));
    }

    @Test
    public void getRatingAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingAdd() throws Exception {
        mockMvc.perform(get("/rating/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getRatingUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/rating/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingUpdateWithException() throws Exception {
        when(ratingService.getRatingById(0)).thenThrow(new IllegalArgumentException("Invalid rating Id:0"));
        mockMvc.perform(get("/rating/update/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingUpdate() throws Exception {
        Rating rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("");
        rating.setSandPRating("");
        rating.setFitchRating("");
        rating.setOrderNumber(0);
        when(ratingService.getRatingById(1)).thenReturn(rating);

        mockMvc.perform(get("/rating/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("rating/update"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getRatingDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/rating/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingDelete() throws Exception {

        mockMvc.perform(get("/rating/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void postRatingValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/rating/validate")
                .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRatingValidate() throws Exception {
        mockMvc.perform(post("/rating/validate")
                    .param("moodysRating", "moody")
                    .param("sandPRating", "sand")
                    .param("fitchRating", "fitch")
                    .param("orderNumber", "10")
                    .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRatingValidateMoodysEmpty() throws Exception {
        this.mockMvc.perform(post("/rating/update/0")
                .param("moodysRating", "")
                .param("sandPRating", "sandPRating")
                .param("fitchRating", "fitchRating")
                .param("orderNumber", "1")
                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));
    }

    @Test
    public void postRatingUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/rating/update/0")
                .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRatingUpdate() throws Exception {
        mockMvc.perform(post("/rating/update/1")
                .param("moodysRating", "moody")
                .param("sandPRating", "sand")
                .param("fitchRating", "fitch")
                .param("orderNumber", "10")
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRatingUpdateFitchRatingEmpty() throws Exception {
        this.mockMvc.perform(post("/rating/update/0")
                .param("moodysRating", "moodysRating")
                .param("sandPRating", "sandPRating")
                .param("fitchRating", "")
                .param("orderNumber", "1")
                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRatingUpdateSandPRatingEmpty() throws Exception {
        this.mockMvc.perform(post("/rating/update/0")
                .param("moodysRating", "moodysRating")
                .param("sandPRating", "")
                .param("fitchRating", "fitchRating")
                .param("orderNumber", "1")
                .with(csrf())
        ).andExpect(redirectedUrl("/rating/list"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingDeleteWithException() throws Exception {
        doThrow(new Exception()).when(ratingService).deleteRating(eq(0));
        mockMvc.perform(get("/rating/delete/0")
                .param("moodysRating", "moody")
                .param("sandPRating", "sand")
                .param("fitchRating", "fitch")
                .param("orderNumber", "10")
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRatingDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid rating Id:0")).when(ratingService).deleteRating(eq(0));
        mockMvc.perform(get("/rating/delete/0")
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/rating/list"))
                .andExpect(model().hasNoErrors());
    }


}
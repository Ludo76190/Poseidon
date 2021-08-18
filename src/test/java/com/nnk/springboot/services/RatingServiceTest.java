package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RatingServiceTest {

    @MockBean
    private RatingRepository ratingRepository;

    @Autowired
    private RatingService ratingService;

    private static Rating rating;

    @BeforeEach
    public void initTest() {
        rating = new Rating();
        rating.setId(1);
        rating.setMoodysRating("");
        rating.setSandPRating("");
        rating.setFitchRating("");
        rating.setOrderNumber(0);
    }

    @Test
    void createRatingTest() throws Exception {
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        ratingService.createRating(rating);
        verify(ratingRepository, times(1)).save(rating);

    }

    @Test
    void updateRatingTest() throws Exception {
        when(ratingRepository.getOne(1)).thenReturn(rating);
        when(ratingRepository.save(any(Rating.class))).thenReturn(rating);
        ratingService.updateRating(rating, 1);
        verify(ratingRepository, times(1)).save(any(Rating.class));
    }

    @Test
    void getAllRatingTest() {
        List<Rating> ratings = new ArrayList<>();
        ratings.add(new Rating());
        when(ratingRepository.findAll()).thenReturn(ratings);
        List<Rating> expectedBibList = ratingService.getAllRating();
        assertThat(expectedBibList).isEqualTo(ratings);
        verify(ratingRepository).findAll();
    }

    @Test
    void getRatingByIdTest() {
        when(ratingRepository.getOne(1)).thenReturn(rating);
        Rating bibListTest = ratingService.getRatingById(1);
        assertThat(bibListTest).isEqualTo(rating);
    }

    @Test
    void deleteRatingByIdTest() throws Exception {
        when(ratingRepository.findById(1)).thenReturn(Optional.of(rating));
        ratingService.deleteRating(1);
        verify(ratingRepository).deleteById(1);
    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void createRating(Rating rating) {
        ratingRepository.save(rating);

    }

    @Override
    public void updateRating(Rating rating, Integer id) {
        Rating updatedRating = getRatingById(id);
        updatedRating.setMoodysRating(rating.getMoodysRating());
        updatedRating.setSandPRating(rating.getSandPRating());
        updatedRating.setFitchRating(rating.getFitchRating());
        updatedRating.setOrderNumber(rating.getOrderNumber());
        ratingRepository.save(updatedRating);

    }

    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    @Override
    public Rating getRatingById(Integer id) {
        return ratingRepository.getOne(id);
    }

    @Override
    public void deleteRating(Integer id) throws Exception {
        ratingRepository.findById(id).orElseThrow(() -> new Exception("Rating not found " + id ));
        ratingRepository.deleteById(id);
    }
}

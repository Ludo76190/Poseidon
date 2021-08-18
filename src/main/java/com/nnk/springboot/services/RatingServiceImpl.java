package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger logger = LogManager.getLogger(RatingServiceImpl.class);

    @Autowired
    private RatingRepository ratingRepository;

    @Override
    public void createRating(Rating rating) {
        ratingRepository.save(rating);
        logger.info("Success create Rating");
    }

    @Override
    public void updateRating(Rating rating, Integer id) {
        Rating updatedRating = getRatingById(id);
        updatedRating.setMoodysRating(rating.getMoodysRating());
        updatedRating.setSandPRating(rating.getSandPRating());
        updatedRating.setFitchRating(rating.getFitchRating());
        updatedRating.setOrderNumber(rating.getOrderNumber());
        ratingRepository.save(updatedRating);
        logger.info("Success update Rating");

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
    public void deleteRating(Integer id) {
        ratingRepository.deleteById(id);
        logger.info("Success delete Rating " + id);
    }
}

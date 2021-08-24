package com.nnk.springboot.services;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of interface RatingService
 */
@Service
public class RatingServiceImpl implements RatingService {

    private static final Logger logger = LogManager.getLogger(RatingServiceImpl.class);

    @Autowired
    private RatingRepository ratingRepository;

    /**
     * Creates Rating.
     * @param rating the Rating create
     */
    @Override
    public void createRating(Rating rating) {
        ratingRepository.save(rating);
        logger.info("Success create Rating");
    }

    /**
     * Updates a Rating
     * @param rating the Rating to update
     * @param id id of the rating to update
     */
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

    /**
     * Get all Rating
     * @return all Rating
     */
    @Override
    public List<Rating> getAllRating() {
        return ratingRepository.findAll();
    }

    /**
     * returns Rating from an id
     * @param id the rating's id
     * @return the rating
     */
    @Override
    public Rating getRatingById(Integer id) {
        return ratingRepository.getOne(id);
    }

    /**
     * delete rating from an id
     * @param id the rating's id
     */
    @Override
    public void deleteRating(Integer id) {
        ratingRepository.deleteById(id);
        logger.info("Success delete Rating " + id);
    }
}

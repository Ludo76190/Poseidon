package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.validation.Valid;

/**
 * Controller for Rating CRUD operations
 */
@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    /**
     * Return Rating page
     *
     * @param model of Rating
     * @return the page of rating
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.getAllRating());
        return "rating/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new rating
     * @return the add rating page
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    /**
     * Create a rating
     * @param rating Rating to create
     * @param result contains errors if Rating is not valid
     * @param model list of Rating
     * @return list of Rating if valid or stay in add page
     */
    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        ratingService.createRating(rating);
        LOGGER.info("Rating added");
        model.addAttribute("rating", ratingService.getAllRating());
        return "redirect:/rating/list";
    }

    /**
     * Return the completed updated page
     *
     * @param id Rating's id to update
     * @param model Rating to update
     * @return update Rating page
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            Rating rating = ratingService.getRatingById(id);
            model.addAttribute("rating", rating);
            return "rating/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting rating " + e.toString());
            return "redirect:/rating/list";
        }
    }

    /**
     * Update a Rating
     *
     * @param id         Rating's id to update
     * @param rating     New Rating with new values
     * @param result     contains errors of rating if not valid
     * @param model      list of Rating
     * @return list of CurvePoint page
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                               BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/update";
        }
        ratingService.updateRating(rating, id);
        model.addAttribute("curvePointList", ratingService.getAllRating());
        LOGGER.info("Update of rating id " + id + " successful");
        return "redirect:/rating/list";
    }

    /**
     * Delete Rating by id
     *
     * @param id    Rating's id to delete
     * @param model List of Rating
     * @return Rating's list page
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        try {
            ratingService.deleteRating(id);
            LOGGER.info("Delete of rating id " + id + " successful");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting rating " + e.toString());
        }
        return "redirect:/rating/list";
    }
}

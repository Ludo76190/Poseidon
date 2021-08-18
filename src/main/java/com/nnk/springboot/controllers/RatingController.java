package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.services.RatingService;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

@Controller
public class RatingController {

    private static final Logger logger = LogManager.getLogger(RatingController.class);

    // TODO: Inject Rating service
    @Autowired
    RatingService ratingService;

    @RequestMapping("/rating/list")
    public String home(Model model)
    {
        // TODO: find all Rating, add to model
        model.addAttribute("ratings", ratingService.getAllRating());
        return "rating/list";
    }

    @GetMapping("/rating/add")
    public String addRatingForm(Rating rating) {
        return "rating/add";
    }

    @PostMapping("/rating/validate")
    public String validate(@Valid Rating rating, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Rating list
        if (result.hasErrors()) {
            logger.error("Error add Rating");
            return "rating/add";
        }
        ratingService.createRating(rating);
        logger.info("Success add Rating");
        model.addAttribute("rating", ratingService.getAllRating());
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Rating by Id and to model then show to the form
        try {
            Rating rating = ratingService.getRatingById(id);
            logger.info ("Success get Rating " + id);
            model.addAttribute("rating", rating);
            return "rating/update";
        } catch (IllegalArgumentException e) {
            logger.error("Error getting Rating " + id);
            e.printStackTrace();
        }
        return "redirect:/rating/list";
    }

    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @Valid Rating rating,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Rating and return Rating list
        if (result.hasErrors()) {
            logger.error("Error updating Rating " + id);
            return "rating/update";
        }
        ratingService.updateRating(rating, id);
        logger.info("Succes update Rating " + id);
        model.addAttribute("rating", ratingService.getAllRating());
        return "redirect:/rating/list";
    }

    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Rating by Id and delete the Rating, return to Rating list
        try {
            ratingService.deleteRating(id);
            logger.info("Success delete Rating " + id);
        } catch (Exception e) {
            logger.error("Error deleting Rating " + id);
            e.printStackTrace();
        }
        model.addAttribute("rating", ratingService.getAllRating());
        return "redirect:/rating/list";
    }
}

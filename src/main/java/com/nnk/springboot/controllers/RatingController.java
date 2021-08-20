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
 * To manage CRUD operations for Rating
 */
@Controller
public class RatingController {

    @Autowired
    private RatingService ratingService;
    
    private static final Logger LOGGER = LoggerFactory.getLogger(RatingController.class);

    /**
     * To return rating page
     * @param model filled with list of all rating
     * @return rating page
     */
    @RequestMapping("/rating/list")
    public String home(Model model) {
        model.addAttribute("ratings", ratingService.getAllRating());
        return "rating/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new rating
     * @return the add form
     */
    @GetMapping("/rating/add")
    public String addRatingForm(Model model) {
        model.addAttribute("rating", new Rating());
        return "rating/add";
    }

    /**
     * To create a rating
     * @param rating the rating entered
     * @param result the eventual errors in the form
     * @param model model of the rating to be created, initialised with a new rating if success
     * @return The add form, either with binding errors or with a new rating
     */
    @PostMapping("/rating/validate")
    public String validate(@ModelAttribute @Valid Rating rating, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "rating/add";
        }
        try {
            ratingService.createRating(rating);
            LOGGER.info("Rating added");
            model.addAttribute("message", "Add successful");
            model.addAttribute("rating", new Rating());
        } catch (Exception e) {
            LOGGER.error("Error during adding rating " + e.toString());
            model.addAttribute("message", "Issue during creating rating, please retry later");
        }
        return "rating/add";
    }

    /**
     * To display the update form initialised with the data of the rating to be updated
     * @param id id of the rating to be updated
     * @param model model with the rating to be updated
     * @param attributes Message to be displayed on redirect page
     * @return update form if success, rating list otherwise
     */
    @GetMapping("/rating/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("rating", ratingService.getRatingById(id));
            return "rating/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting rating " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/rating/list";
        }
    }

    /**
     * To update a rating 
     * @param id id of the rating to be updated
     * @param rating Updated data for the rating
     * @param result the eventual errors in the form
     * @param attributes Message to be displayed on redirect page
     * @return rating list if success, update form with errors otherwise
     */
    @PostMapping("/rating/update/{id}")
    public String updateRating(@PathVariable("id") Integer id, @ModelAttribute @Valid Rating rating,
                             BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            rating.setId(id);
            return "rating/update";
        }
        try {
            ratingService.updateRating(rating, id);
            LOGGER.info("Update of rating id " + id + " successful");
            attributes.addFlashAttribute("message", "Update successful");            
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting rating " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during updating rating " + e.toString());
            attributes.addFlashAttribute("message", "Issue during updating, please retry later");
        }
        return "redirect:/rating/list";
    }

    /**
     * To delete a rating
     * @param id id of the rating to be updated
     * @param attributes Message to be displayed on redirect page
     * @return rating list page
     */
    @GetMapping("/rating/delete/{id}")
    public String deleteRating(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            ratingService.deleteRating(id);
            LOGGER.info("Delete of rating id " + id + " successful");
            attributes.addFlashAttribute("message", "Delete successful");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting rating " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during deleting rating " + e.toString());
            attributes.addFlashAttribute("message", "Issue during deleting, please retry later");
        }
        return "redirect:/rating/list";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.configuration.exception.AlreadyExistException;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
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
 * To manage CRUD operations for User
 */
@Controller
public class UserController {

    private static final Logger LOGGER = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    /**
     * To return user page
     * @param model filled with list of all user
     * @return user page
     */
    @RequestMapping("/user/list")
    public String home(Model model) {
        model.addAttribute("users", userService.getAllUser());
        return "user/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new user
     * @return the add form
     */
    @GetMapping("/user/add")
    public String addUser(Model model) {
        model.addAttribute("user", new User());
        return "user/add";
    }

    /**
     * To create a user
     * @param user the user entered
     * @param result the eventual errors in the form
     * @param model model of the user to be created, initialised with a new user if success
     * @return The add form, either with binding errors or with a new user
     */
    @PostMapping("/user/validate")
    public String validate(@ModelAttribute @Valid User user, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            LOGGER.debug("User validate form Ok");
            try {
                userService.createUser(user);
                model.addAttribute("message", "Add successful");
                model.addAttribute("user", new User());
                LOGGER.info("User added");
            } catch (AlreadyExistException e) {
                LOGGER.error("Error during adding User " + e.getMessage());
                model.addAttribute("message", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error during adding User " + e);
                model.addAttribute("message", "Issue during creating user, please retry later");
            }
        }
        return "user/add";
    }

    /**
     * To display the update form initialised with the data of the user to be updated
     * @param id id of the user to be updated
     * @param model model with the user to be updated
     * @param attributes Message to be displayed on redirect page
     * @return update form if success, user list otherwise
     */
    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("user", userService.getUserById(id));
            return "user/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting User " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/user/list";
        }
    }

    /**
     * To update a user 
     * @param id id of the user to be updated
     * @param user Updated data for the user
     * @param result the eventual errors in the form
     * @param attributes Message to be displayed on redirect page
     * @param model model to display User already exist message
     * @return user list if success, update form with errors otherwise
     */
    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @ModelAttribute("user") @Valid User user,
                             BindingResult result, RedirectAttributes attributes, Model model) {
        if (result.hasErrors()) {
            LOGGER.debug("Error in user update form");
            user.setId(id);
            return "user/update";
        }

        try {
            userService.updateUser(user, id);
            attributes.addFlashAttribute("message", "Update successful");
            LOGGER.info("User id " + id + " updated");
            return "redirect:/user/list";
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            LOGGER.error("Error during updating user id " + id + " " + e.toString());
            return "redirect:/user/list";
        } catch (AlreadyExistException e) {
            LOGGER.error("Error during updating User " + e.getMessage());
            model.addAttribute("user", userService.getUserById(id));
            model.addAttribute("message", e.getMessage());
            return "user/update";
        } catch (Exception e) {
            attributes.addFlashAttribute("message", "Issue during updating, please retry later");
            LOGGER.error("Error during updating user id " + id + " " + e.toString());
            return "redirect:/user/list";
        }

    }

    /**
     * To delete a user
     * @param id id of the user to be updated
     * @param attributes Message to be displayed on redirect page
     * @return user list page
     */
    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            userService.deleteUser(id);
            attributes.addFlashAttribute("message", "Delete successful");
            LOGGER.info("User id "+ id + " deleted");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            LOGGER.error("Error during deleting User id " + id + " " + e.toString());
        } catch (Exception e) {
            attributes.addFlashAttribute("message", "Issue during deleting, please retry later");
            LOGGER.error("Error during deleting User id " + id + " " + e.toString());
        }
        return "redirect:/user/list";
    }
}

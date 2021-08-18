package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.UserService;
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
public class UserController {

    private static final Logger logger = LogManager.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @RequestMapping("/user/list")
    public String home(Model model)
    {
        model.addAttribute("users", userService.getAllUser());
        return "user/list";
    }

    @GetMapping("/user/add")
    public String addUser(User bid) {
        return "user/add";
    }

    @PostMapping("/user/validate")
    public String validate(@Valid User user, BindingResult result, Model model) throws Exception {
        if (!result.hasErrors()) {
            logger.info("Validate User OK");
            userService.createUser(user);
            logger.info("Success add user " + user.getUsername());
            model.addAttribute("users", userService.getAllUser());
            return "redirect:/user/list";
        }
        return "user/add";
    }

    @GetMapping("/user/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        User user = userService.getUserById(id);
        if (user == null) {
            logger.error("Error getting user " + id);
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }
        logger.info("Success get user " + id);
        model.addAttribute("user", user);
        return "user/update";
    }

    @PostMapping("/user/update/{id}")
    public String updateUser(@PathVariable("id") Integer id, @Valid User user,
                             BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            logger.error("Error get user " + id);
            return "user/update";
        }
        userService.updateUser(user,id);
        logger.info("Success update user " + id);
        model.addAttribute("users", userService.getAllUser());
        return "redirect:/user/list";
    }

    @GetMapping("/user/delete/{id}")
    public String deleteUser(@PathVariable("id") Integer id, Model model) throws Exception {
        User user = userService.getUserById(id);
        if ( user == null) {
            logger.error("Error User not found");
            throw new IllegalArgumentException("Invalid user Id:" + id);
        }
        userService.deleteUser(id);
        logger.info("Success delete user " + id);
        model.addAttribute("users", userService.getAllUser());
        return "redirect:/user/list";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
 * Controller for RuleName CRUD operations
 */
@Controller
public class RuleNameController {

    @Autowired
    private RuleNameService ruleNameService;

    private static final Logger LOGGER = LoggerFactory.getLogger(RuleNameController.class);

    /**
     * Return RuleName page
     *
     * @param model of RuleName
     * @return the page of RuleName
     */
    @GetMapping("/ruleName/list")
    public String home(Model model)
    {
        model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
        return "ruleName/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new RuleName
     * @return the add RuleName page
     */
    @GetMapping("/ruleName/add")
    public String addRuleForm(Model model) {
        model.addAttribute("ruleName", new RuleName());
        return "ruleName/add";
    }

    /**
     * Create a curve point
     * @param ruleName RuleName to create
     * @param result contains errors if ruleName is not valid
     * @param model list of RuleName
     * @return list of RuleName if valid or stay in add page
     */
    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/add";
        }
        ruleNameService.createRuleName(ruleName);
        LOGGER.info("Rule Name added");
        model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        return "redirect:ruleName/list";
    }

    /**
     * Return the completed updated page
     *
     * @param id RuleName's id to update
     * @param model RuleName to update
     * @return update RuleName page
     */
    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            RuleName ruleName = ruleNameService.getRuleNameById(id);
            model.addAttribute("ruleName",ruleName);
            return "ruleName/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting rule name " + e);
            return "redirect:/ruleName/list";
        }
    }

    /**
     * Update a CurvePoint
     *
     * @param id         RuleName's id to update
     * @param ruleName New CurvePoint with new values
     * @param result     contains errors of ruleName if not valid
     * @param model      list of RuleName
     * @return list of RuleName page
     */
    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                                 BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "ruleName/update";
        }
        ruleNameService.updateRuleName(ruleName, id);
        model.addAttribute("ruleNameList",ruleNameService.getAllRuleName());
        LOGGER.info("Rule name id " + id + "updated");
        return "redirect:/ruleName/list";
    }

    /**
     * Delete RuleName by id
     *
     * @param id    RuleName's id to delete
     * @param model List of RuleName
     * @return RuleName's list page
     */
    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        try {
            ruleNameService.deleteRuleName(id);
            LOGGER.info("Rule name id " + id + "deleted");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting rule name " + e.toString());
        }
        return "redirect:/ruleName/list";
    }
}

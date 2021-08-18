package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
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
public class RuleNameController {

    private static final Logger logger = LogManager.getLogger(RuleNameController.class);

    // TODO: Inject RuleName service
    @Autowired
    RuleNameService ruleNameService;

    @RequestMapping("/ruleName/list")
    public String home(Model model)
    {
        // TODO: find all RuleName, add to model
        model.addAttribute("ruleNames", ruleNameService.getAllRuleName());
        return "ruleName/list";
    }

    @GetMapping("/ruleName/add")
    public String addRuleForm(RuleName bid) {
        return "ruleName/add";
    }

    @PostMapping("/ruleName/validate")
    public String validate(@Valid RuleName ruleName, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return RuleName list
        if (result.hasErrors()) {
            logger.error("Error add RuleName");
            return "ruleName/add";
        }
        ruleNameService.createRuleName(ruleName);
        logger.info("Success add RuleName");
        model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get RuleName by Id and to model then show to the form
        try {
            RuleName ruleName = ruleNameService.getRuleNameById(id);
            logger.info ("Success get RuleName " + id);
            model.addAttribute("ruleName", ruleName);
            return "ruleName/update";
        } catch (IllegalArgumentException e) {
            logger.error("Error getting RuleName " + id);
            e.printStackTrace();
        }
        return "redirect:/ruleName/list";
    }

    @PostMapping("/ruleName/update/{id}")
    public String updateRuleName(@PathVariable("id") Integer id, @Valid RuleName ruleName,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update RuleName and return RuleName list
        if (result.hasErrors()) {
            logger.error("Error updating RuleName " + id);
            return "RuleName/update";
        }
        ruleNameService.updateRuleName(ruleName, id);
        logger.info("Succes update RuleName " + id);
        model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }

    @GetMapping("/ruleName/delete/{id}")
    public String deleteRuleName(@PathVariable("id") Integer id, Model model) {
        // TODO: Find RuleName by Id and delete the RuleName, return to Rule list
        try {
            ruleNameService.deleteRuleName(id);
            logger.info("Success delete RuleName " + id);
        } catch (Exception e) {
            logger.error("Error deleting RuleName " + id);
            e.printStackTrace();
        }
        model.addAttribute("ruleName", ruleNameService.getAllRuleName());
        return "redirect:/ruleName/list";
    }
}

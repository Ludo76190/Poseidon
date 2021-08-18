package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
public class TradeController {

    private static final Logger logger = LogManager.getLogger(TradeController.class);

    // TODO: Inject Trade service
    @Autowired
    TradeService tradeService;

    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        // TODO: find all Trade, add to model
        model.addAttribute("trades", tradeService.getAllTrade());
        return "trade/list";
    }

    @GetMapping("/trade/add")
    public String addUser(Trade bid) {
        return "trade/add";
    }

    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Trade list
        if (result.hasErrors()) {
            logger.error("Error add Trade");
            return "trade/add";
        }
        tradeService.createTrade(trade);
        logger.info("Success add Trade");
        model.addAttribute("trade", tradeService.getAllTrade());
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Trade by Id and to model then show to the form
        try {
            Trade trade = tradeService.getTradeById(id);
            logger.info ("Success get Trade " + id);
            model.addAttribute("trade", trade);
            return "trade/update";
        } catch (IllegalArgumentException e) {
            logger.error("Error getting Trade " + id);
            e.printStackTrace();
        }
        return "trade/update";
    }

    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Trade and return Trade list
        if (result.hasErrors()) {
            logger.error("Error updating Trade " + id);
            return "trade/update";
        }
        tradeService.updateTrade(trade, id);
        logger.info("Succes update Trade " + id);
        model.addAttribute("trade", tradeService.getAllTrade());
        return "redirect:/trade/list";
    }

    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Trade by Id and delete the Trade, return to Trade list
        try {
            tradeService.deleteTrade(id);
            logger.info("Success delete Trade " + id);
        } catch (Exception e) {
            logger.error("Error deleting Trade " + id);
            e.printStackTrace();
        }
        model.addAttribute("trade", tradeService.getAllTrade());
        return "redirect:/trade/list";
    }
}

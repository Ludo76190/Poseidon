package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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
 * Controller for Trade CRUD operations
 */
@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    /**
     * Return Trade page
     *
     * @param model of Trade
     * @return the page of Trade
     */
    @GetMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeService.getAllTrade());
        return "trade/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new Trade
     * @return the add Trade page
     */
    @GetMapping("/trade/add")
    public String addTrade(Model model) {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    /**
     * Create a curve point
     * @param trade Trade to create
     * @param result contains errors if Trade is not valid
     * @param model list of Trade
     * @return list of Trade if valid or stay in add page
     */
    @PostMapping("/trade/validate")
    public String validate(@Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        tradeService.createTrade(trade);
        LOGGER.info("Trade added");
        model.addAttribute("tradeList",tradeService.getAllTrade());
        return "redirect:/trade/list";
    }

    /**
     * Return the completed updated page
     *
     * @param id Trade's id to update
     * @param model Trade to update
     * @return update Trade page
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            Trade trade = tradeService.getTradeById(id);
            model.addAttribute("trade", trade);
            return "trade/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting Trade " + e.toString());
            return "redirect:/trade/list";
        }

    }

    /**
     * Update a Trade
     *
     * @param id         Trade's id to update
     * @param trade      New Trade with new values
     * @param result     contains errors of trade if not valid
     * @param model      list of Trade
     * @return list of Trade page
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @Valid Trade trade,
                              BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/update";
        }
        tradeService.updateTrade(trade, id);
        model.addAttribute("tradeList",tradeService.getAllTrade());
        LOGGER.info("Trade id " + id + " updated");
        return "redirect:/trade/list";
    }

    /**
     * Delete Trade by id
     *
     * @param id    Trade's id to delete
     * @param model List of Trade
     * @return Trade's list page
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, Model model) {
        try {
            tradeService.deleteTrade(id);
            LOGGER.info("Trade id "+ id + " deleted");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting Trade id " + id + " " + e.toString());
        }
        return "redirect:/trade/list";
    }
}

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
 * To manage CRUD operations for Trade
 */
@Controller
public class TradeController {

    @Autowired
    private TradeService tradeService;

    private static final Logger LOGGER = LoggerFactory.getLogger(TradeController.class);

    /**
     * To return trade page
     * @param model filled with list of all trade
     * @return trade page
     */
    @RequestMapping("/trade/list")
    public String home(Model model)
    {
        model.addAttribute("trades", tradeService.getAllTrade());
        return "trade/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new trade
     * @return the add form
     */
    @GetMapping("/trade/add")
    public String addUser(Model model) {
        model.addAttribute("trade", new Trade());
        return "trade/add";
    }

    /**
     * To create a trade
     * @param trade the trade entered
     * @param result the eventual errors in the form
     * @param model model of the trade to be created, initialised with a new trade if success
     * @return The add form, either with binding errors or with a new trade
     */
    @PostMapping("/trade/validate")
    public String validate(@ModelAttribute @Valid Trade trade, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "trade/add";
        }
        try {
            tradeService.createTrade(trade);
            model.addAttribute("message", "Add successful");
            model.addAttribute("trade", new Trade());
            LOGGER.info("Trade added");
        } catch (Exception e) {
            LOGGER.error("Error during adding Trade " + e.toString());
            model.addAttribute("message", "Issue during creating trade, please retry later");
        }
        return "trade/add";
    }

    /**
     * To display the update form initialised with the data of the trade to be updated
     * @param id id of the trade to be updated
     * @param model model with the trade to be updated
     * @param attributes Message to be displayed on redirect page
     * @return update form if success, trade list otherwise
     */
    @GetMapping("/trade/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("trade", tradeService.getTradeById(id));
            return "trade/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting Trade " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/trade/list";
        }

    }

    /**
     * To update a trade 
     * @param id id of the trade to be updated
     * @param trade Updated data for the trade
     * @param result the eventual errors in the form
     * @param attributes Message to be displayed on redirect page
     * @return trade list if success, update form with errors otherwise
     */
    @PostMapping("/trade/update/{id}")
    public String updateTrade(@PathVariable("id") Integer id, @ModelAttribute @Valid Trade trade,
                             BindingResult result, Model model, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            trade.setTradeId(id);
            return "trade/update";
        }
        try {
            tradeService.updateTrade(trade, id);
            attributes.addFlashAttribute("message", "Update successful");
            LOGGER.info("Trade id " + id + " updated");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            LOGGER.error("Error during updating trade id " + id + " " + e.toString());
        }
        return "redirect:/trade/list";
    }

    /**
     * To delete a trade
     * @param id id of the trade to be updated
     * @param attributes Message to be displayed on redirect page
     * @return trade list page
     */
    @GetMapping("/trade/delete/{id}")
    public String deleteTrade(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            tradeService.deleteTrade(id);
            attributes.addFlashAttribute("message", "Delete successful");
            LOGGER.info("Trade id "+ id + " deleted");
        } catch (IllegalArgumentException e) {
            attributes.addFlashAttribute("message", e.getMessage());
            LOGGER.error("Error during deleting Trade id " + id + " " + e.toString());
        }
        return "redirect:/trade/list";
    }
}

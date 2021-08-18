package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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
public class BidListController {
    // TODO: Inject Bid service
    @Autowired
    BidListService bidListService;

    @RequestMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "bidList/list";
    }

    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return bid list
        if(result.hasErrors()) {
            return "bidList/add";
        }
        bidListService.createBidList(bid);
        model.addAttribute("bidlist", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get Bid by Id and to model then show to the form
        try {
            BidList bidList = bidListService.getBidListById(id);
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }
        return "redirect:/bidList/list";
    }

    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Bid and return list Bid
        if (result.hasErrors()) {
            return "bidList/update";
        }

        bidListService.updateBidList(bidList, id);
        model.addAttribute("bidList", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }

    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Bid by Id and delete the bid, return to Bid list
        try {
            bidListService.deleteBidList(id);
        } catch (Exception e) {
            e.printStackTrace();
        }
        model.addAttribute("bidList", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }
}

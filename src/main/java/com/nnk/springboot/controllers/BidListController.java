package com.nnk.springboot.controllers;

import com.nnk.springboot.config.exception.AlreadyExistException;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.services.BidListService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import javax.validation.Valid;

/**
 * Controller for BidList CRUD operations
 */
@Controller
public class BidListController {

    @Autowired
    private BidListService bidListService;

    private static final Logger LOGGER = LoggerFactory.getLogger(BidListController.class);

    /**
     * Return bidList page
     *
     * @param model list of BidList
     * @return the page of bidList list
     */
    @GetMapping("/bidList/list")
    public String home(Model model) {
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "bidList/list";
    }

    /**
     * Return the add bidList page
     *
     * @param bid bidList to add
     * @return add bidList page
     */
    @GetMapping("/bidList/add")
    public String addBidForm(BidList bid) {
        return "bidList/add";
    }

    /**
     * Create a bidList if valid
     *
     * @param bid    bidList to create
     * @param result contains errors if bidList is not valid
     * @param model  list of BidList
     * @return list of BidList if valid or stay in add page
     */
    @PostMapping("/bidList/validate")
    public String validate(@Valid BidList bid, BindingResult result, Model model) {
        if (!result.hasErrors()) {
            LOGGER.debug("User validate form Ok");
            try {
                bidListService.createBidList(bid);
                model.addAttribute("message", "Add successful");
                model.addAttribute("bidList", new BidList());
                LOGGER.info("BidList added");
            } catch (AlreadyExistException e) {
                LOGGER.error("Error during adding BidList " + e.getMessage());
                model.addAttribute("message", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error during adding BidList " + e);
                model.addAttribute("message", "Issue during creating BidList, please retry later");
            }
        }
        return "bidList/add";
    }

    /**
     * Return the completed updated page
     *
     * @param id    bidList's id to update
     * @param model bidList to Update
     * @return update BidList page
     */
    @GetMapping("/bidList/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            BidList bidList = bidListService.getBidListById(id);
            model.addAttribute("bidList", bidList);
            return "bidList/update";
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during getting BindList " + e.toString());
            return "redirect:/bidList/list";
        }
    }

    /**
     * Update a bidList
     *
     * @param id      bidlist's id to update
     * @param bidList new bidlist with new values
     * @param result  contains errors of bidlist if not valid
     * @param model   list of BidList
     * @return list of BidList page
     */
    @PostMapping("/bidList/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid BidList bidList,
                            BindingResult result, Model model) throws Exception {
        if (!result.hasErrors()) {
            LOGGER.debug("User validate form Ok");
            try {
                bidListService.updateBidList(bidList, id);
                model.addAttribute("message", "Update successful");
                model.addAttribute("bidLists", new BidList());
                LOGGER.info("BidList added");
            } catch (AlreadyExistException e) {
                LOGGER.error("Error during adding BidList " + e.getMessage());
                model.addAttribute("message", e.getMessage());
            } catch (Exception e) {
                LOGGER.error("Error during adding BidList " + e);
                model.addAttribute("message", "Issue during creating BidList, please retry later");
            }
        }
        return "bidList/update";
    }
        /*if (result.hasErrors()) {
            return "bidList/update";
        }
        bidListService.updateBidList(bidList, id);
        model.addAttribute("bidLists", bidListService.getAllBidList());
        return "redirect:/bidList/list";
    }*/

    /**
     * Delete bidList by Id
     *
     * @param id    bidList's id to delete
     * @param model List of bidList
     * @return BidList's List page
     */
    @GetMapping("/bidList/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            bidListService.deleteBidList(id);
            LOGGER.info("BidList id " + id + " deleted");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting bind list id " + id + " " + e.toString());
        }
        return "redirect:/bidList/list";
    }
}

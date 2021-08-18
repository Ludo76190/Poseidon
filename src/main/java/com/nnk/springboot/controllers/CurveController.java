package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
public class CurveController {

    private static final Logger logger = LogManager.getLogger(CurveController.class);

    // TODO: Inject Curve Point service
    @Autowired
    private CurvePointService curvePointService;

    @RequestMapping("/curvePoint/list")
    public String home(Model model)
    {
        // TODO: find all Curve Point, add to model
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoint());
        return "curvePoint/list";
    }

    @GetMapping("/curvePoint/add")
    public String addBidForm(CurvePoint bid) {
        return "curvePoint/add";
    }

    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        // TODO: check data valid and save to db, after saving return Curve list
        if (result.hasErrors()) {
            logger.error("Error add CurvePoint");
            return "curvePoint/add";
        }
        curvePointService.createCurvePoint(curvePoint);
        logger.info("Success add CurvePoint");
        model.addAttribute("curvePoint", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        // TODO: get CurvePoint by Id and to model then show to the form
        try {
            CurvePoint curvePoint = curvePointService.getCurvePointById(id);
            logger.info ("Success get CurvePoint " + id);
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        } catch (IllegalArgumentException e) {
            logger.error("Error getting CurvePoint " + id);
            e.printStackTrace();
        }
        return "redirect:/curvePoint/list";
    }

    @PostMapping("/curvePoint/update/{id}")
    public String updateBid(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                             BindingResult result, Model model) {
        // TODO: check required fields, if valid call service to update Curve and return Curve list
        if (result.hasErrors()) {
            logger.error("Error updating CurvePoint " + id);
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(curvePoint, id);
        logger.info("Succes update CurvePoint " + id);
        model.addAttribute("curvePoint", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }

    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        // TODO: Find Curve by Id and delete the Curve, return to Curve list
        try {
            curvePointService.deleteCurvePoint(id);
            logger.info("Success delete CurvePoint " + id);
        } catch (Exception e) {
            logger.error("Error deleting CurvePoint " + id);
            e.printStackTrace();
        }
        model.addAttribute("curvePoint", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
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
 * Controller for CurvePoint CRUD operations
 */
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CurveController.class);

    /**
     * Return CurvePoint page
     *
     * @param model of CurvePoint
     * @return the page of CurvePoint
     */
    @GetMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoint());
        return "curvePoint/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new CurvePoint
     * @return the add CurvePoint page
     */
    @GetMapping("/curvePoint/add")
    public String addCurveForm(Model model) {
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    /**
     * Create a curve point
     * @param curvePoint CurvePoint to create
     * @param result contains errors if curvePoint is not valid
     * @param model list of CurvePoint
     * @return list of CurvePoint if valid or stay in add page
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@Valid CurvePoint curvePoint, BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        curvePointService.createCurvePoint(curvePoint);
        LOGGER.info("Curve point added");
        model.addAttribute("curvePoint", curvePointService.getAllCurvePoint());
        return "redirect:/curvePoint/list";
    }

    /**
     * Return the completed updated page
     *
     * @param id CurvePoint's id to update
     * @param model CurvePoint to update
     * @return update CurvePoint page
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model) {
        try {
            CurvePoint curvePoint = curvePointService.getCurvePointById(id);
            model.addAttribute("curvePoint", curvePoint);
            return "curvePoint/update";
        } catch (IllegalArgumentException e){
            LOGGER.error("Error during getting curve point " + e);
            return "redirect:/curvePoint/list";
        }
    }

    /**
     * Update a CurvePoint
     *
     * @param id         CurvePoint's id to update
     * @param curvePoint New CurvePoint with new values
     * @param result     contains errors of curvePoint if not valid
     * @param model      list of CurvePoint
     * @return list of CurvePoint page
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurvePoint(@PathVariable("id") Integer id, @Valid CurvePoint curvePoint,
                            BindingResult result, Model model) {
        if (result.hasErrors()) {
            return "curvePoint/update";
        }
        curvePointService.updateCurvePoint(curvePoint, id);
        model.addAttribute("curvePointList", curvePointService.getAllCurvePoint());
        LOGGER.info("Curve point id " + id + "updated");
        return "redirect:/curvePoint/list";
    }

    /**
     * Delete CurvePoint by id
     *
     * @param id    CurvePoint's id to delete
     * @param model List of CurvePoint
     * @return CurvePoint's list page
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteBid(@PathVariable("id") Integer id, Model model) {
        try {
            curvePointService.deleteCurvePoint(id);
            LOGGER.info("Curve point id " + id + " deleted");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting curve point id " + id + " " + e.toString());
        }
        return "redirect:/curvePoint/list";
    }
}

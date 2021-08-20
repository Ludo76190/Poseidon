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
 * To manage CRUD operations for CurvePoint
 */
@Controller
public class CurveController {

    @Autowired
    private CurvePointService curvePointService;

    private static final Logger LOGGER = LoggerFactory.getLogger(CurveController.class);

    /**
     * To return curve point page
     * @param model filled with list of all curve point
     * @return curve point page
     */
    @RequestMapping("/curvePoint/list")
    public String home(Model model) {
        model.addAttribute("curvePoints", curvePointService.getAllCurvePoint());
        return "curvePoint/list";
    }

    /**
     * To display the add form
     * @param model initialised with a new curve point
     * @return the add form
     */
    @GetMapping("/curvePoint/add")
    public String addCurveForm(Model model) {
        model.addAttribute("curvePoint", new CurvePoint());
        return "curvePoint/add";
    }

    /**
     * To create a curve point
     * @param curvePoint the curve point entered
     * @param result the eventual errors in the form
     * @param model model of the curve point to be created, initialised with a new curve point if success
     * @return The add form, either with binding errors or with a new curve point
     */
    @PostMapping("/curvePoint/validate")
    public String validate(@ModelAttribute @Valid CurvePoint curvePoint, BindingResult result, Model model) {

        if (result.hasErrors()) {
            return "curvePoint/add";
        }
        try {
            curvePointService.createCurvePoint(curvePoint);
            LOGGER.info("Curve point added");
            model.addAttribute("curvePoint", new CurvePoint());
            model.addAttribute("message", "Add successful");
        } catch (Exception e) {
            LOGGER.error("Error during adding curve point " + e.toString());
            model.addAttribute("message", "Issue during creating curve point, please retry later");
        }
        return "curvePoint/add";
    }

    /**
     * To display the update form initialised with the data of the curve point to be updated
     * @param id id of the curve point to be updated
     * @param model model with the curve point to be updated
     * @param attributes Message to be displayed on redirect page
     * @return update form if success, curve point list otherwise
     */
    @GetMapping("/curvePoint/update/{id}")
    public String showUpdateForm(@PathVariable("id") Integer id, Model model, RedirectAttributes attributes) {
        try {
            model.addAttribute("curvePoint", curvePointService.getCurvePointById(id));
            return "curvePoint/update";
        } catch (IllegalArgumentException e){
            LOGGER.error("Error during getting curve point " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/curvePoint/list";
        }
    }

    /**
     * To update a curve point 
     * @param id id of the curve point to be updated
     * @param curvePoint Updated data for the curvePoint
     * @param result the eventual errors in the form
     * @param attributes Message to be displayed on redirect page
     * @return curve point list if success, update form with errors otherwise
     */
    @PostMapping("/curvePoint/update/{id}")
    public String updateCurve(@PathVariable("id") Integer id, @ModelAttribute @Valid CurvePoint curvePoint,
                              BindingResult result, RedirectAttributes attributes) {
        if (result.hasErrors()) {
            curvePoint.setId(id);
            return "curvePoint/update";
        }
        try {
            curvePointService.updateCurvePoint(curvePoint, id);
            LOGGER.info("Curve point id " + id + "updated");
            attributes.addFlashAttribute("message", "Update successful");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during updating curve point " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during updating curve point " + e.toString());
            attributes.addFlashAttribute("message", "Issue during updating, please retry later");
        }
        return "redirect:/curvePoint/list";
    }

    /**
     * To delete a curve point
     * @param id id of the curve point to be updated
     * @param attributes Message to be displayed on redirect page
     * @return curve point list page
     */
    @GetMapping("/curvePoint/delete/{id}")
    public String deleteCurve(@PathVariable("id") Integer id, RedirectAttributes attributes) {
        try {
            curvePointService.deleteCurvePoint(id);
            LOGGER.info("Curve point id " + id + " deleted");
            attributes.addFlashAttribute("message", "Delete successful");
        } catch (IllegalArgumentException e) {
            LOGGER.error("Error during deleting curve point id " + id + " " + e.toString());
            attributes.addFlashAttribute("message", e.getMessage());
        } catch (Exception e) {
            LOGGER.error("Error during deleting curve point " + e.toString());
            attributes.addFlashAttribute("message", "Issue during deleting, please retry later");
        }
        return "redirect:/curvePoint/list";
    }
}

package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.services.CurvePointService;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CurveController.class)
class CurveControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CurvePointService curvePointService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getCurvePointListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getCurvePointList() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(1);
        curvePoint.setTerm(8.0);
        curvePoint.setValue(1.0);
        curvePoint.setId(1);
        List<CurvePoint> curvePoints = new ArrayList<>();
        curvePoints.add(curvePoint);
        when(curvePointService.getAllCurvePoint()).thenReturn(curvePoints);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("curvePoints", curvePoints));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getCurvePointListAsUSER() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(1);
        curvePoint.setTerm(8.0);
        curvePoint.setValue(1.0);
        curvePoint.setId(1);
        List<CurvePoint> curvePoints = new ArrayList<>();
        curvePoints.add(curvePoint);
        when(curvePointService.getAllCurvePoint()).thenReturn(curvePoints);

        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("curvePoints", curvePoints))
                .andExpect(content().string(not(containsString("&nbsp;|&nbsp;<a href=\"/user/list\">User</a>"))));
    }

    @Test
    public void getCurvePointAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getCurvePointAdd() throws Exception {
        mockMvc.perform(get("/curvePoint/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getCurvePointUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/curvePoint/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getCurvePointUpdate() throws Exception {
        CurvePoint curvePoint = new CurvePoint();
        curvePoint.setCurveId(1);
        curvePoint.setTerm(8.0);
        curvePoint.setValue(1.0);
        curvePoint.setId(1);
        when(curvePointService.getCurvePointById(1)).thenReturn(curvePoint);

        mockMvc.perform(get("/curvePoint/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getCurvePointDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/curvePoint/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getCurvePointDelete() throws Exception {

        mockMvc.perform(get("/curvePoint/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void postCurvePointValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointValidate() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                    .param("curveId", "10")
                    .param("term", "10")
                    .param("value", "10")
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointValidateCurveIdNull() throws Exception {
        mockMvc.perform(post("/curvePoint/validate")
                    .param("curveId", " ")
                    .param("term", "10")
                    .param("value", "10")
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("curvePoint", "curveId", "NotNull"));
    }

    @Test
    public void postCurvePointUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/curvePoint/update/0")
                .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointUpdate() throws Exception {
        mockMvc.perform(post("/curvePoint/update/0")
                    .param("curveId", "10")
                    .param("term", "10")
                    .param("value", "10")
                    .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointUpdateCurveIdNull() throws Exception {
        mockMvc.perform(post("/curvePoint/update/0")
                    .param("curveId", "")
                    .param("term", "10")
                    .param("value", "10")
                    .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("curvePoint/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("curvePoint", "curveId", "NotNull"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointValidateWithException() throws Exception {
        this.mockMvc.perform(post("/curvePoint/validate")
                .param("curveId", "90")
                .param("term", "A")
                .param("value", "B")
                .with(csrf())
        ).andExpect(model().hasErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointUpdateWithException() throws Exception {
        this.mockMvc.perform(post("/curvePoint/update/0")
                .param("curveId", "91")
                .param("term", "12.0")
                .param("value", "A")
                .with(csrf())
        ).andExpect(model().hasErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postCurvePointUpdateWithIllegalArgumentException() throws Exception {
        mockMvc.perform(post("/curvePoint/update/0")
                        .param("curveId", "12")
                        .param("term", "10")
                        .param("value", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getCurvePointDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid curve point Id:0")).when(curvePointService).deleteCurvePoint(eq(0));
        mockMvc.perform(get("/curvePoint/delete/0")
                .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/curvePoint/list"))
                .andExpect(model().hasNoErrors());
    }

}
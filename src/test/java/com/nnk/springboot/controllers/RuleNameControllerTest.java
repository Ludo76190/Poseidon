package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.services.RuleNameService;
import com.nnk.springboot.services.UserDetailsServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = RuleNameController.class)
class RuleNameControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RuleNameService ruleNameService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getRuleNameListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameList() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setName("Rule name");
        ruleName.setDescription("Rule description");
        ruleName.setId(1);
        List<RuleName> ruleNames = new ArrayList<>();
        ruleNames.add(ruleName);
        when(ruleNameService.getAllRuleName()).thenReturn(ruleNames);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("ruleNames", ruleNames));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getRuleNameListAsUSER() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setName("Rule name");
        ruleName.setDescription("Rule description");
        ruleName.setId(1);
        List<RuleName> ruleNames = new ArrayList<>();
        ruleNames.add(ruleName);
        when(ruleNameService.getAllRuleName()).thenReturn(ruleNames);

        mockMvc.perform(get("/ruleName/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("ruleNames", ruleNames))
                .andExpect(content().string(not(containsString("&nbsp;|&nbsp;<a href=\"/user/list\">User</a>"))));
    }

    @Test
    public void getRuleNameAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameAdd() throws Exception {
        mockMvc.perform(get("/ruleName/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getRuleNameUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/ruleName/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameUpdateWithException() throws Exception {
        when(ruleNameService.getRuleNameById(0)).thenThrow(new IllegalArgumentException("Invalid curve point Id:0"));
        mockMvc.perform(get("/ruleName/update/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid curve point Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameUpdate() throws Exception {
        RuleName ruleName = new RuleName();
        ruleName.setName("Rule name");
        ruleName.setDescription("Description");
        ruleName.setId(1);
        when(ruleNameService.getRuleNameById(1)).thenReturn(ruleName);

        mockMvc.perform(get("/ruleName/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("ruleName", ruleName));
    }

    @Test
    public void getRuleNameDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/ruleName/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameDelete() throws Exception {

        mockMvc.perform(get("/ruleName/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Delete successful"));
    }

    @Test
    public void postRuleNameValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRuleNameValidate() throws Exception {
        mockMvc.perform(post("/ruleName/validate")
                        .param("name", "Rule name")
                        .param("description", "Rule description")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("ruleName/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Add successful"));
    }

    @Test
    public void postRuleNameUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/ruleName/update/0")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRuleNameUpdate() throws Exception {
        mockMvc.perform(post("/ruleName/update/0")
                        .param("name", "Rule name")
                        .param("description", "Rule description")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Update successful"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postRuleNameUpdateWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid rule name Id:0")).when(ruleNameService).updateRuleName(any(RuleName.class), eq(0));
        mockMvc.perform(post("/ruleName/update/0")
                        .param("name", "rule name")
                        .param("description", "Rule description")
                        .param("json", "json")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid rule name Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getRuleNameDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid rule name Id:0")).when(ruleNameService).deleteRuleName(eq(0));
        mockMvc.perform(get("/ruleName/delete/0")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/ruleName/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid rule name Id:0"));
    }


}
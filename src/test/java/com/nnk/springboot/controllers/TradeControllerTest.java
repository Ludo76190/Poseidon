package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.services.TradeService;
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

@WebMvcTest(controllers = TradeController.class)
class TradeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TradeService tradeService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getTradeListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeList() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("test");
        trade.setType("Type1");
        trade.setBuyQuantity(10.0);
        List<Trade> trades = new ArrayList<>();
        trades.add(trade);
        when(tradeService.getAllTrade()).thenReturn(trades);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("trades", trades));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getTradeListAsUSER() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("test");
        trade.setType("Type1");
        trade.setBuyQuantity(10.0);
        List<Trade> trades = new ArrayList<>();
        trades.add(trade);
        when(tradeService.getAllTrade()).thenReturn(trades);

        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("trades", trades))
                .andExpect(content().string(not(containsString("&nbsp;|&nbsp;<a href=\"/user/list\">User</a>"))));
    }

    @Test
    public void getTradeAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeAdd() throws Exception {
        mockMvc.perform(get("/trade/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getTradeUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/trade/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeUpdateWithException() throws Exception {
        when(tradeService.getTradeById(0)).thenThrow(new IllegalArgumentException("Invalid trade Id:0"));
        mockMvc.perform(get("/trade/update/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid trade Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeUpdate() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("test");
        trade.setType("Type1");
        trade.setBuyQuantity(10.0);
        when(tradeService.getTradeById(1)).thenReturn(trade);

        mockMvc.perform(get("/trade/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("trade", trade));
    }

    @Test
    public void getTradeDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/trade/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeDelete() throws Exception {

        mockMvc.perform(get("/trade/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Delete successful"));
    }

    @Test
    public void postTradeValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeValidate() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "test")
                        .param("type", "type")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Add successful"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeValidateAccountEmpty() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", " ")
                        .param("type", "type")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade", "account", "NotBlank"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeValidateTypeEmpty() throws Exception {
        mockMvc.perform(post("/trade/validate")
                        .param("account", "account")
                        .param("type", "")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade", "type", "NotBlank"));
    }

    @Test
    public void postTradeUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/trade/update/0")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeUpdate() throws Exception {

        mockMvc.perform(post("/trade/update/0")
                        .param("account", "test")
                        .param("type", "type")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeUpdateAccountEmpty() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("test");
        trade.setType("Type1");
        trade.setBuyQuantity(10.0);
        when(tradeService.getTradeById(1)).thenReturn(trade);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", "")
                        .param("type", "type")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade", "account", "NotBlank"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeUpdateTypeEmpty() throws Exception {
        Trade trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("test");
        trade.setType("Type1");
        trade.setBuyQuantity(10.0);
        when(tradeService.getTradeById(1)).thenReturn(trade);

        mockMvc.perform(post("/trade/update/1")
                        .param("account", "account")
                        .param("type", "")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("trade/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("trade", "type", "NotBlank"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postTradeUpdateWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid trade Id:0")).when(tradeService).updateTrade(any(Trade.class), eq(0));
        mockMvc.perform(post("/trade/update/0")
                        .param("account", "test")
                        .param("type", "type")
                        .param("buyQuantity", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid trade Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getTradeDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid trade Id:0")).when(tradeService).deleteTrade(eq(0));
        mockMvc.perform(get("/trade/delete/0")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/trade/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid trade Id:0"));
    }


}
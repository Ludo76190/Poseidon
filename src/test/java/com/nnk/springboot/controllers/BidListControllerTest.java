package com.nnk.springboot.controllers;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.services.BidListService;
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

@WebMvcTest(controllers = BidListController.class)
class BidListControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BidListService bidListService;

    @MockBean
    private UserDetailsServiceImpl userDetailsService;

    @Test
    public void getBidListListWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListList() throws Exception {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);
        List<BidList> bidLists = new ArrayList<>();
        bidLists.add(bidList);
        when(bidListService.getAllBidList()).thenReturn(bidLists);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("bidLists", bidLists));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void getBidListListConnectedAsUser() throws Exception {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);
        List<BidList> bidLists = new ArrayList<>();
        bidLists.add(bidList);
        when(bidListService.getAllBidList()).thenReturn(bidLists);

        mockMvc.perform(get("/bidList/list"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("bidLists", bidLists))
                .andExpect(content().string(not(containsString("&nbsp;|&nbsp;<a href=\"/user/list\">User</a>"))));
    }

    @Test
    public void getBidListAddWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListAdd() throws Exception {
        mockMvc.perform(get("/bidList/add"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasNoErrors());
    }

    @Test
    public void getBidListUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/bidList/update/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListUpdateWithException() throws Exception {
        when(bidListService.getBidListById(0)).thenThrow(new IllegalArgumentException("Invalid bid list Id:0"));
        mockMvc.perform(get("/bidList/update/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid bid list Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListValidateWithException() throws Exception {
        doThrow(new SQLException()).when(bidListService).createBidList(any(BidList.class));
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "test")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Issue during creating, please retry later"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListUpdateWithException() throws Exception {
        doThrow(new Exception()).when(bidListService).updateBidList(any(BidList.class), eq(0));
        mockMvc.perform(post("/bidList/update/0")
                        .param("account", "test")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Issue during updating, please retry later"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListUpdateWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid bid list Id:0")).when(bidListService).updateBidList(any(BidList.class), eq(0));
        mockMvc.perform(post("/bidList/update/0")
                        .param("account", "test")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid bid list Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListDeleteWithIllegalArgumentException() throws Exception {
        doThrow(new IllegalArgumentException("Invalid bid list Id:0")).when(bidListService).deleteBidList(eq(0));
        mockMvc.perform(get("/bidList/delete/0")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Invalid bid list Id:0"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListUpdate() throws Exception {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);
        when(bidListService.getBidListById(1)).thenReturn(bidList);

        mockMvc.perform(get("/bidList/update/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("bidList", bidList));
    }

    @Test
    public void getBidListDeleteWithoutAuthentication() throws Exception {
        mockMvc.perform(get("/bidList/delete/0"))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void getBidListDelete() throws Exception {

        mockMvc.perform(get("/bidList/delete/0"))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors())
                .andExpect(flash().attribute("message", "Delete successful"));
    }

    @Test
    public void postBidListValidateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListValidate() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "test")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasNoErrors())
                .andExpect(model().attribute("message", "Add successful"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListValidateAccountEmpty() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", " ")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList", "account", "NotBlank"));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListValidateTypeEmpty() throws Exception {
        mockMvc.perform(post("/bidList/validate")
                        .param("account", "account")
                        .param("type", "")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/add"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList", "type", "NotBlank"));
    }

    @Test
    public void postBidListUpdateWithoutAuthentication() throws Exception {
        mockMvc.perform(post("/bidList/update/0")
                        .with(csrf()))
                .andExpect(status().is(302));
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListUpdate() throws Exception {

        mockMvc.perform(post("/bidList/update/0")
                        .param("account", "test")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().is(302))
                .andExpect(view().name("redirect:/bidList/list"))
                .andExpect(model().hasNoErrors());
    }

    @WithMockUser(username = "admin", authorities = {"ADMIN"})
    @Test
    public void postBidListUpdateAccountEmpty() throws Exception {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);
        when(bidListService.getBidListById(1)).thenReturn(bidList);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "")
                        .param("type", "type")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList", "account", "NotBlank"));
    }

    @WithMockUser(username = "user", authorities = {"USER"})
    @Test
    public void postBidListUpdateTypeEmpty() throws Exception {
        BidList bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("Type1");
        bidList.setBidQuantity(10.0);
        when(bidListService.getBidListById(1)).thenReturn(bidList);

        mockMvc.perform(post("/bidList/update/1")
                        .param("account", "account")
                        .param("type", "")
                        .param("bidQuantity", "10")
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(view().name("bidList/update"))
                .andExpect(model().hasErrors())
                .andExpect(model().attributeHasFieldErrorCode("bidList", "type", "NotBlank"));
    }


}
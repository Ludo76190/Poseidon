package com.nnk.springboot.integration;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.repositories.UserRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.redirectedUrlPattern;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
public class TradeControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private TradeRepository tradeRepository;

    @Autowired
    private UserRepository userRepository;

    Trade trade = new Trade();
    User userAdmin = new User();
    User userUser = new User();

    @BeforeEach
    public void setUp() {
        trade.setAccount("account");
        trade.setType("type");
        trade.setBuyQuantity(10.0);
        tradeRepository.save(trade);

        userAdmin.setId(1);
        userAdmin.setUsername("test");
        userAdmin.setFullname("Test");
        userAdmin.setPassword("Test12345!");
        userAdmin.setRole("ADMIN");
        userRepository.save(userAdmin);

        userUser.setId(2);
        userUser.setUsername("test2");
        userUser.setFullname("Test2");
        userUser.setPassword("Test54321!");
        userUser.setRole("USER");
        userRepository.save(userUser);
    }

    @AfterEach
    public void clearAll() {
        this.tradeRepository.deleteAll();
        this.userRepository.deleteAll();

    }

    @Test
    public void getTradePageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/user/trade"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ADMIN"})
    public void getTradePageAdminTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test2", authorities = {"USER"})
    public void getTradePageUserTest() throws Exception {
        mockMvc.perform(get("/trade/list"))
                .andExpect(status().isOk());

    }

}

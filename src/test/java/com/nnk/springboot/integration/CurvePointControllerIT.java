package com.nnk.springboot.integration;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointControllerIT {

    @Autowired
    MockMvc mockMvc;

    @Autowired
    private CurvePointRepository curvePointRepository;

    @Autowired
    private UserRepository userRepository;

    CurvePoint curvePoint = new CurvePoint();
    User userAdmin = new User();
    User userUser = new User();

    @BeforeEach
    public void setUp() {
        curvePoint.setCurveId(1);
        curvePoint.setTerm(2.0);
        curvePoint.setValue(3.0);
        curvePointRepository.save(curvePoint);

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
        this.curvePointRepository.deleteAll();
        this.userRepository.deleteAll();

    }

    @Test
    public void getCurvePointPageWithoutAuthenticationTest() throws Exception {
        mockMvc.perform(get("/user/curvePointList"))
                .andExpect(status().is(302))
                .andExpect(redirectedUrlPattern("**/login"));
    }

    @Test
    @WithMockUser(username = "test", authorities = {"ADMIN"})
    public void getCurvePointPageAdminTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk());

    }

    @Test
    @WithMockUser(username = "test2", authorities = {"USER"})
    public void getCurvePointPageUserTest() throws Exception {
        mockMvc.perform(get("/curvePoint/list"))
                .andExpect(status().isOk());

    }

}

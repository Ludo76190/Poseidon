package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@SpringBootTest
public class RuleNameServiceTest {

    @MockBean
    private RuleNameRepository ruleNameRepository;

    @Autowired
    private RuleNameService ruleNameService;

    private static RuleName ruleName;

    @BeforeEach
    public void initTest() {
        ruleName = new RuleName();
        ruleName.setName("Rule name");
        ruleName.setDescription("Rule description");
        ruleName.setId(1);
    }

    @Test
    void createRuleNameTest() throws Exception {
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        ruleNameService.createRuleName(ruleName);
        verify(ruleNameRepository, times(1)).save(ruleName);

    }

    @Test
    void updateRuleNameTest() throws Exception {
        when(ruleNameRepository.getOne(1)).thenReturn(ruleName);
        when(ruleNameRepository.save(any(RuleName.class))).thenReturn(ruleName);
        ruleNameService.updateRuleName(ruleName, 1);
        verify(ruleNameRepository, times(1)).save(any(RuleName.class));
    }

    @Test
    void getAllRuleNameTest() {
        List<RuleName> ruleNames = new ArrayList<>();
        ruleNames.add(new RuleName());
        when(ruleNameRepository.findAll()).thenReturn(ruleNames);
        List<RuleName> expectedBibList = ruleNameService.getAllRuleName();
        assertThat(expectedBibList).isEqualTo(ruleNames);
        verify(ruleNameRepository).findAll();
    }

    @Test
    void getRuleNameByIdTest() {
        when(ruleNameRepository.getOne(1)).thenReturn(ruleName);
        RuleName bibListTest = ruleNameService.getRuleNameById(1);
        assertThat(bibListTest).isEqualTo(ruleName);
    }

    @Test
    void deleteRuleNameByIdTest() throws Exception {
        when(ruleNameRepository.findById(1)).thenReturn(Optional.of(ruleName));
        ruleNameService.deleteRuleName(1);
        verify(ruleNameRepository).deleteById(1);
    }
}

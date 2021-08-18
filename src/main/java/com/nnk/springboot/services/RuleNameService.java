package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;

import java.util.List;

public interface RuleNameService {
    void createRuleName(RuleName rule);
    void updateRuleName(RuleName rule, Integer id);
    List<RuleName> getAllRuleName();
    RuleName getRuleNameById(Integer id);
    void deleteRuleName(Integer id);

}

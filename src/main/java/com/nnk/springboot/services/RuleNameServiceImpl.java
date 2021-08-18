package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class RuleNameServiceImpl implements RuleNameService{

    @Autowired
    private RuleNameRepository ruleNameRepository;

    @Override
    public void createRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
    }

    @Override
    public void updateRuleName(RuleName ruleName, Integer id) {
        RuleName updatedRuleName = getRuleNameById(id);
        updatedRuleName.setName(ruleName.getName());
        updatedRuleName.setDescription(ruleName.getDescription());
        updatedRuleName.setJson(ruleName.getJson());
        updatedRuleName.setTemplate(ruleName.getTemplate());
        updatedRuleName.setSqlStr(ruleName.getSqlStr());
        updatedRuleName.setSqlPart(ruleName.getSqlPart());
        ruleNameRepository.save(updatedRuleName);

    }

    @Override
    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    @Override
    public RuleName getRuleNameById(Integer id) {
        return ruleNameRepository.getOne(id);
    }

    @Override
    public void deleteRuleName(Integer id) throws Exception {
        ruleNameRepository.findById(id).orElseThrow(() -> new Exception("RuleName not found " + id ));
        ruleNameRepository.deleteById(id);
    }
}

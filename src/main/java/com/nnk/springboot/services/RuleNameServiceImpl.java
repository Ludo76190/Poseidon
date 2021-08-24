package com.nnk.springboot.services;

import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Implementation of interface RuleNameService
 */
@Service
public class RuleNameServiceImpl implements RuleNameService{

    private static final Logger logger = LogManager.getLogger(RatingServiceImpl.class);

    @Autowired
    private RuleNameRepository ruleNameRepository;

    /**
     * Creates RuleName.
     * @param ruleName the RuleName to create
     */
    @Override
    public void createRuleName(RuleName ruleName) {
        ruleNameRepository.save(ruleName);
        logger.info("Success create RuleName");
    }

    /**
     * Updates a RuleName
     * @param ruleName the bidList to update
     * @param id id of the ruleName to update
     */
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
        logger.info("Success update RuleName");
    }

    /**
     * Get all RuleName
     * @return all RuleName
     */
    @Override
    public List<RuleName> getAllRuleName() {
        return ruleNameRepository.findAll();
    }

    /**
     * returns RuleName from an id
     * @param id the ruleName's id
     * @return the ruleName
     */
    @Override
    public RuleName getRuleNameById(Integer id) {
        return ruleNameRepository.getOne(id);
    }

    /**
     * delete ruleName from an id
     * @param id the ruleName's id
     */
    @Override
    public void deleteRuleName(Integer id) {
        ruleNameRepository.deleteById(id);
        logger.info("Success delete RuleName " + id);
    }
}

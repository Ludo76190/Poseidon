package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of interface TradeService
 */
@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger logger = LogManager.getLogger(TradeServiceImpl.class);

    @Autowired
    private TradeRepository tradeRepository;

    /**
     * Creates Trade.
     * @param trade the Trade to create
     */
    @Override
    public void createTrade(Trade trade) {
        trade.setCreationDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(trade);
        logger.info("Success create Trade");
    }

    /**
     * Updates a Trade
     * @param trade the trade to update
     * @param id id of the trade to update
     */
    @Override
    public void updateTrade(Trade trade, Integer id) {

        Trade updatedTrade = getTradeById(id);
        updatedTrade.setAccount(trade.getAccount());
        updatedTrade.setBuyQuantity(trade.getBuyQuantity());
        updatedTrade.setType(trade.getType());
        updatedTrade.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(updatedTrade);
        logger.info("Success update Trade");
    }

    /**
     * Get all Trade
     * @return all Trade
     */
    @Override
    public List<Trade> getAllTrade() {
        return tradeRepository.findAll();
    }

    /**
     * returns Trade from an id
     * @param id the trade's id
     * @return the trade
     */
    @Override
    public Trade getTradeById(Integer id) {
        return tradeRepository.getOne(id);
    }

    /**
     * delete trade from an id
     * @param id the trade's id
     */
    @Override
    public void deleteTrade(Integer id) {
        tradeRepository.deleteById(id);
        logger.info("Success delete Trade " + id);
    }
}

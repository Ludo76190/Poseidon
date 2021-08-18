package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    private static final Logger logger = LogManager.getLogger(TradeServiceImpl.class);

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public void createTrade(Trade trade) {
        trade.setCreationDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(trade);
        logger.info("Success create Trade");
    }

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

    @Override
    public List<Trade> getAllTrade() {
        return tradeRepository.findAll();
    }

    @Override
    public Trade getTradeById(Integer id) {
        return tradeRepository.getOne(id);
    }

    @Override
    public void deleteTrade(Integer id) {
        tradeRepository.deleteById(id);
        logger.info("Success delete Trade " + id);
    }
}

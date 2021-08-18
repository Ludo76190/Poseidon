package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class TradeServiceImpl implements TradeService {

    @Autowired
    private TradeRepository tradeRepository;

    @Override
    public void createTrade(Trade trade) {
        trade.setCreationDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(trade);
    }

    @Override
    public void updateTrade(Trade trade, Integer id) {

        Trade updatedTrade = getTradeById(id);
        updatedTrade.setAccount(trade.getAccount());
        updatedTrade.setBuyQuantity(trade.getBuyQuantity());
        updatedTrade.setType(trade.getType());
        updatedTrade.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        tradeRepository.save(updatedTrade);
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
    public void deleteTrade(Integer id) throws Exception {
        tradeRepository.findById(id).orElseThrow(() -> new Exception("RuleName not found " + id ));
        tradeRepository.deleteById(id);

    }
}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;

import java.util.List;

public interface TradeService {
    void createTrade(Trade trade);
    void updateTrade(Trade trade, Integer id);
    List<Trade> getAllTrade();
    Trade getTradeById(Integer id);
    void deleteTrade(Integer id);
}

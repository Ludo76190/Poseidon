package com.nnk.springboot.services;

import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.TradeRepository;
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
public class TradeServiceTest {

    @MockBean
    private TradeRepository tradeRepository;

    @Autowired
    private TradeService tradeService;

    private static Trade trade;

    @BeforeEach
    public void initTest() {
        trade = new Trade();
        trade.setTradeId(1);
        trade.setAccount("");
        trade.setType("");
        trade.setBuyQuantity(10.0);
    }

    @Test
    void createTradeTest() throws Exception {
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        tradeService.createTrade(trade);
        verify(tradeRepository, times(1)).save(trade);

    }

    @Test
    void updateTradeTest() throws Exception {
        when(tradeRepository.getOne(1)).thenReturn(trade);
        when(tradeRepository.save(any(Trade.class))).thenReturn(trade);
        tradeService.updateTrade(trade, 1);
        verify(tradeRepository, times(1)).save(any(Trade.class));
    }

    @Test
    void getAllTradeTest() {
        List<Trade> trades = new ArrayList<>();
        trades.add(new Trade());
        when(tradeRepository.findAll()).thenReturn(trades);
        List<Trade> expectedBibList = tradeService.getAllTrade();
        assertThat(expectedBibList).isEqualTo(trades);
        verify(tradeRepository).findAll();
    }

    @Test
    void getTradeByIdTest() {
        when(tradeRepository.getOne(1)).thenReturn(trade);
        Trade bibListTest = tradeService.getTradeById(1);
        assertThat(bibListTest).isEqualTo(trade);
    }

    @Test
    void deleteTradeByIdTest() throws Exception {
        when(tradeRepository.findById(1)).thenReturn(Optional.of(trade));
        tradeService.deleteTrade(1);
        verify(tradeRepository).deleteById(1);
    }
}

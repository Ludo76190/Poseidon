package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
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
public class BidListServiceTest {

    @MockBean
    private BidListRepository bidListRepository;

    @Autowired
    private BidListService bidListService;

    private static BidList bidList;

    @BeforeEach
    public void init() {
        bidList = new BidList();
        bidList.setBidListId(1);
        bidList.setAccount("test");
        bidList.setType("test");
        bidList.setBidQuantity(10.0);
    }

    @Test
    void createBidListTest() throws Exception {
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        bidListService.createBidList(bidList);
        verify(bidListRepository, times(1)).save(bidList);

    }

    @Test
    void updateBidListTest() throws Exception {
        when(bidListRepository.getOne(1)).thenReturn(bidList);
        when(bidListRepository.save(any(BidList.class))).thenReturn(bidList);
        bidListService.updateBidList(bidList, 1);
        verify(bidListRepository, times(1)).save(any(BidList.class));
    }

    @Test
    void getAllBidListTest() {
        List<BidList> bidLists = new ArrayList<>();
        bidLists.add(new BidList());
        when(bidListRepository.findAll()).thenReturn(bidLists);
        List<BidList> expectedBibList = bidListService.getAllBidList();
        assertThat(expectedBibList).isEqualTo(bidLists);
        verify(bidListRepository).findAll();
    }

    @Test
    void getBidListByIdTest() {
        when(bidListRepository.getOne(1)).thenReturn(bidList);
        BidList bibListTest = bidListService.getBidListById(1);
        assertThat(bibListTest).isEqualTo(bidList);
    }

    @Test
    void deleteBidListByIdTest() throws Exception {
        when(bidListRepository.findById(1)).thenReturn(Optional.of(bidList));
        bidListService.deleteBidList(1);
        verify(bidListRepository).deleteById(1);
    }
}

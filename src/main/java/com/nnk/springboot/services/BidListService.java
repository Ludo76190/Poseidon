package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;

import java.util.List;

public interface BidListService {

    void createBidList(BidList bid);
    void updateBidList(BidList bid, Integer id) ;
    List<BidList> getAllBidList();
    BidList getBidListById(Integer id);
    void deleteBidList(Integer id);

}

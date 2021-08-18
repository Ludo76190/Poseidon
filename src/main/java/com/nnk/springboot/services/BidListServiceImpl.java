package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BidListServiceImpl implements BidListService {

    @Autowired
    private BidListRepository bidListRepository;

    @Override
    public void createBidList(BidList bidList) {
        bidListRepository.save(bidList);
    }

    @Override
    public void updateBidList(BidList bidList, Integer id) {
        BidList updatedBidList = getBidListById(id);
        updatedBidList.setAccount(bidList.getAccount());
        updatedBidList.setBidQuantity(bidList.getBidQuantity());
        updatedBidList.setType(bidList.getType());
        updatedBidList.setRevisionDate(new Timestamp(System.currentTimeMillis()));
        bidListRepository.save(updatedBidList);
    }

    @Override
    public List<BidList> getAllBidList() {
        return null;
    }

    @Override
    public BidList getBidListById(Integer id) {
        return null;
    }

    @Override
    public void deleteBidList(Integer id) {

    }


}

package com.nnk.springboot.services;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

@Service
public class BidListServiceImpl implements BidListService{

    private static final Logger logger = LogManager.getLogger(BidListServiceImpl.class);

    @Autowired
    private BidListRepository bidListRepository;

    @Override
    public void createBidList(BidList bidList)  {
        bidList.setCreationDate(new Timestamp(System.currentTimeMillis()));
        bidListRepository.save(bidList);
        logger.info("BidList created");
    }

    @Override
    public void updateBidList(BidList bidList, Integer id) {
        BidList updatedBidList = getBidListById(id);
        updatedBidList.setAccount(bidList.getAccount());
        updatedBidList.setType(bidList.getType());
        updatedBidList.setBidQuantity(bidList.getBidQuantity());
        updatedBidList.setRevisionDate(new Timestamp((System.currentTimeMillis())));
        bidListRepository.save(updatedBidList);
        logger.info("BidList updated");
    }

    @Override
    public List<BidList> getAllBidList() {
        return bidListRepository.findAll();
    }

    @Override
    public BidList getBidListById(Integer id) {
        return bidListRepository.getOne(id);
    }

    @Override
    public void deleteBidList(Integer id) {
        bidListRepository.deleteById(id);
        logger.info("Success delete BidList" + id);
    }
}

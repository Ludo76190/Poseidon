package com.nnk.springboot.services;

import com.nnk.springboot.config.exception.AlreadyExistException;
import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.repositories.BidListRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of interface BidListService
 */
@Service
public class BidListServiceImpl implements BidListService{

    private static final Logger logger = LogManager.getLogger(BidListServiceImpl.class);

    @Autowired
    private BidListRepository bidListRepository;

    /**
     * Creates BidList.
     * @param bidList the BidList to create
     */
    @Override
    public void createBidList(BidList bidList) throws Exception {
        if (bidListRepository.findBidListByAccount(bidList.getAccount()) != null) {
            logger.error("bidList "+ bidList.getAccount() +" already exist");
            throw new AlreadyExistException("BidList " + bidList.getAccount() + " already exists.");
        }
        bidList.setCreationDate(new Timestamp(System.currentTimeMillis()));
        bidListRepository.save(bidList);
        logger.info("BidList created");
    }

    /**
     * Updates a BidList
     * @param bidList the bidList to update
     * @param id id of the bidList to update
     */
    @Override
    public void updateBidList(BidList bidList, Integer id) throws Exception {
        if (bidListRepository.findBidListByAccount(bidList.getAccount()) != null) {
            logger.error("bidList "+ bidList.getAccount() +" already exist");
            throw new AlreadyExistException("BidList " + bidList.getAccount() + " already exists.");
        }
        BidList updatedBidList = getBidListById(id);
        updatedBidList.setAccount(bidList.getAccount());
        updatedBidList.setType(bidList.getType());
        updatedBidList.setBidQuantity(bidList.getBidQuantity());
        updatedBidList.setRevisionDate(new Timestamp((System.currentTimeMillis())));
        bidListRepository.save(updatedBidList);
        logger.info("BidList updated");
    }

    /**
     * Get all BidList
     * @return all BidList
     */
    @Override
    public List<BidList> getAllBidList() {
        return bidListRepository.findAll();
    }

    /**
     * returns BidList from an id
     * @param id the bidList's id
     * @return the bidList
     */
    @Override
    public BidList getBidListById(Integer id) {
        return bidListRepository.getOne(id);
    }

    /**
     * delete bidList from an id
     * @param id the bidList's id
     */
    @Override
    public void deleteBidList(Integer id) {
        bidListRepository.deleteById(id);
        logger.info("Success delete BidList" + id);
    }
}

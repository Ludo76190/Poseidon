package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.List;

/**
 * Implementation of interface CurvePointService
 */

@Service
public class CurvePointServiceImpl implements CurvePointService{

    private static final Logger logger = LogManager.getLogger(CurvePointServiceImpl.class);

    @Autowired
    private CurvePointRepository curvePointRepository;

    /**
     * Creates CurvePoint.
     * @param curvePoint the BidList to be created
     */
    @Override
    public void createCurvePoint(CurvePoint curvePoint) {
        curvePoint.setCreationDate(new Timestamp(System.currentTimeMillis()));
        curvePointRepository.save(curvePoint);
        logger.info("Success create CurvePoint");
    }

    /**
     * Updates a CurvePont
     * @param curvePoint the bidList to update
     * @param id id of the curvePoint to update
     */
    @Override
    public void updateCurvePoint(CurvePoint curvePoint, Integer id) {

        CurvePoint updatedCurvePoint = getCurvePointById(id);
        updatedCurvePoint.setCurveId(curvePoint.getCurveId());
        updatedCurvePoint.setTerm(curvePoint.getTerm());
        updatedCurvePoint.setValue(curvePoint.getValue());
        curvePointRepository.save(updatedCurvePoint);
        logger.info("Success update CurvePoint");
    }

    /**
     * Get all CurvePoint
     * @return all CurvePoint
     */
    @Override
    public List<CurvePoint> getAllCurvePoint() {
        return curvePointRepository.findAll();
    }

    /**
     * returns CurvePoint from an id
     * @param id the CurvePoint's id
     * @return the CurvePoint
     */
    @Override
    public CurvePoint getCurvePointById(Integer id) {
        return curvePointRepository.getOne(id);
    }

    /**
     * delete CurvePoint from an id
     * @param id the CurvePoint's id
     */
    @Override
    public void deleteCurvePoint(Integer id) {
        curvePointRepository.deleteById(id);
        logger.info("Success delete CurvePoint" + id);
    }
}

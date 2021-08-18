package com.nnk.springboot.services;

import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.CurvePointRepository;
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
public class CurvePointServiceTest {

    @MockBean
    private CurvePointRepository curvePointRepository;

    @Autowired
    private CurvePointService curvePointService;

    private static CurvePoint curvePoint;

    @BeforeEach
    public void initTest() {
        curvePoint = new CurvePoint();
        curvePoint.setCurveId(1);
        curvePoint.setTerm(10.0);
        curvePoint.setValue(3.0);
        curvePoint.setId(1);
    }

    @Test
    void createCurvePointTest() throws Exception {
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        curvePointService.createCurvePoint(curvePoint);
        verify(curvePointRepository, times(1)).save(curvePoint);

    }

    @Test
    void updateCurvePointTest() throws Exception {
        when(curvePointRepository.getOne(1)).thenReturn(curvePoint);
        when(curvePointRepository.save(any(CurvePoint.class))).thenReturn(curvePoint);
        curvePointService.updateCurvePoint(curvePoint, 1);
        verify(curvePointRepository, times(1)).save(any(CurvePoint.class));
    }

    @Test
    void getAllCurvePointTest() {
        List<CurvePoint> curvePoints = new ArrayList<>();
        curvePoints.add(new CurvePoint());
        when(curvePointRepository.findAll()).thenReturn(curvePoints);
        List<CurvePoint> expectedBibList = curvePointService.getAllCurvePoint();
        assertThat(expectedBibList).isEqualTo(curvePoints);
        verify(curvePointRepository).findAll();
    }

    @Test
    void getCurvePointByIdTest() {
        when(curvePointRepository.getOne(1)).thenReturn(curvePoint);
        CurvePoint bibListTest = curvePointService.getCurvePointById(1);
        assertThat(bibListTest).isEqualTo(curvePoint);
    }

    @Test
    void deleteCurvePointByIdTest() throws Exception {
        when(curvePointRepository.findById(1)).thenReturn(Optional.of(curvePoint));
        curvePointService.deleteCurvePoint(1);
        verify(curvePointRepository).deleteById(1);
    }
    
}

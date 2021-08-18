package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;


@Entity
@Table(name = "curvepoint")
@Getter
@Setter
public class CurvePoint {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id ;
    @NotNull(message = "must not be null")
    @Min(value = -128, message = "The value must be between -128 and 127")
    @Max(value = 127, message = "The value must be between -128 and 127")
    @Column(name="curveid")
    private Integer curveId ;
    @Column(name="asofdate")
    private Timestamp asOfDate ;
    private Double term ;
    private Double value ;
    @Column(name="creationdate")
    private Timestamp creationDate;
}

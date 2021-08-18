package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.*;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Getter
@Setter
public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id ;

    @NotNull(message = "must not be null")
    private Integer curveId ;

    private Timestamp asOfDate ;
    private Double term ;
    private Double value ;
    private Timestamp creationDate;
}

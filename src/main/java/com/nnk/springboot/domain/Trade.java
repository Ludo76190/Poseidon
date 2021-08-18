package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;


@Entity
@Table(name = "trade")
@Getter
@Setter
public class Trade {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer tradeId ;
    @NotBlank(message = "Account is mandatory")

    private String account ;
    @NotBlank(message = "Type is mandatory")

    private String type ;
    private Double buyQuantity ;
    private Double sellQuantity ;
    private Double buyPrice ;
    private Double sellPrice ;
    private String benchmark ;
    private Timestamp tradeDate ;
    private String security ;
    private String status ;
    private String trader ;
    private String book ;
    private String creationName ;
    private Timestamp creationDate ;
    private String revisionName ;
    private Timestamp revisionDate ;
    private String dealName ;
    private String dealType ;
    private String sourceListId ;
    private String side ;
}

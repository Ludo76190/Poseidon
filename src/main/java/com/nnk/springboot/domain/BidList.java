package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Getter
@Setter
public class BidList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer bidListId;

    @NotBlank(message = "Account is mandatory")
    private String account ;

    @NotBlank(message = "Type is mandatory")
    private String type ;

    private Double bidQuantity ;
    private Double askQuantity ;
    private Double bid ;
    private Double ask ;
    private String benchmark ;
    private Timestamp bidListDate ;
    private String commentary ;
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
    private String side;
}

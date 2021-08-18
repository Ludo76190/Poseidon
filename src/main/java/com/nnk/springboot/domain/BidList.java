package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "bidlist")
@Getter
@Setter
@NoArgsConstructor
public class BidList {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    @Column(name="bidlistid")
    private Integer BidListId;

    @NotBlank(message = "Account is mandatory")
    private String account;

    @NotBlank(message = "Type is mandatory")
    private String type;

    @Column(name="bidquantity")
    private Double bidQuantity;

    @Column(name="askquantity")
    private Double askQuantity;

    private Double bid;
    private Double ask;
    private String benchmark;

    @Column(name="bidlistdate")
    private Timestamp bidListDate;

    private String commentary;
    private String security;
    private String status;
    private String trader;
    private String book;

    @Column(name="creationname")
    private String creationName ;

    @Column(name="creationdate")
    private Timestamp creationDate ;

    @Column(name="revisionname")
    private String revisionName ;

    @Column(name="revisiondate")
    private Timestamp revisionDate ;

    @Column(name="dealname")
    private String dealName ;

    @Column(name="dealtype")
    private String dealType ;

    @Column(name="sourcelistid")
    private String sourceListId ;

    private String side;

    public BidList(String account, String type, double bidQuantity) {
        this.account = account;
        this.type = type;
        this.bidQuantity = bidQuantity;
    }
}

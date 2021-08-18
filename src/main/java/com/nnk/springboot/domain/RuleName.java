package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@Getter
@Setter
public class RuleName {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id ;
    @NotBlank(message = "Name is mandatory")
    private String name ;
    private String description ;
    private String json ;
    private String template ;
    private String sqlStr ;
    private String sqlPart ;
}

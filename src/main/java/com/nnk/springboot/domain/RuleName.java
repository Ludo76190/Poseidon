package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.sql.Timestamp;

@Entity
@Table(name = "rulename")
@Getter
@Setter
@NoArgsConstructor
public class RuleName {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id ;

    private String name ;
    private String description ;
    private String json ;
    private String template ;

    @Column(name="sqlstr")
    private String sqlStr ;

    @Column(name="sqlpart")
    private String sqlPart ;

    public RuleName(String name, String description, String json, String template, String sqlStr, String sqlPart) {
        this.name = name;
        this.description = description;
        this.json = json;
        this.template = template;
        this.sqlStr = sqlStr;
        this.sqlPart = sqlPart;
    }
}

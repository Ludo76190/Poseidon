package com.nnk.springboot.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.sql.Timestamp;

@Entity
@Table(name = "rating")
@Getter
@Setter
@NoArgsConstructor
public class Rating {
    @Id
    @GeneratedValue(strategy= GenerationType.IDENTITY)
    private Integer id;

    @Column(name="moodysrating")
    private String moodysRating;

    @Column(name="sandprating")
    private String sandPRating;

    @Column(name="fitchrating")
    private String fitchRating;

    @Column(name="ordernumber")
    private Integer orderNumber;

    public Rating(String moodysrating, String sandpRating, String fitchrating, int ordernumber) {
        this.moodysRating = moodysrating;
        this.sandPRating = sandpRating;
        this.fitchRating = fitchrating;
        this.orderNumber = ordernumber;
    }
}

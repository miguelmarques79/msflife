package com.msg.life.app.entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Setter
@Getter
@Entity
public class Mortality {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String country;
    @Column(precision = 10, scale = 2)
    private BigDecimal txFemale;
    @Column(precision = 10, scale = 2)
    private BigDecimal txMale;
    private int yearInterval;
    private long malePopulation;
    private long femalePopulation;


}

package com.mobilki.covidapp.api.model;

import java.io.Serializable;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Exercise implements Serializable {
    private String name;
    private int minReps;
    private int maxReps;
}

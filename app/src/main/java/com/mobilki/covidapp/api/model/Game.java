package com.mobilki.covidapp.api.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Game {
    private String titlePl;
    private String titleEn;
    private String descriptionPl;
    private String descriptionEn;
    private int playersMax;
    private int playersMin;
    private int ageMax;
    private int ageMin;
    private String genrePl;
    private String genreEn;
    private String time;
    private String link;
}

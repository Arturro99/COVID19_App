package com.mobilki.covidapp.api;

import org.json.JSONException;

import java.util.List;

import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.exceptions.*;

public interface FilmDatabaseApi {

    void getTitle(String endPoint, String id) throws JSONException;
    int getYearOfRelease(String endPoint, String id);
    String getRatings(String endPoint, String id);
    String getDescription(String endPoint, String id);
    List<String> getGenres(String endPoint, String id);
    String getImageUrl(String endPoint, String id);
    List<String> getActors(String endPoint, String id);
    String getActorName(String endPoint, String id);
    String getActorDateOfBirth(String endPoint, String id);
    String getActorHeight(String endPoint, String id);
    String getActorBirthPlace(String endPoint, String id);
    List<Film>getFilms();
}

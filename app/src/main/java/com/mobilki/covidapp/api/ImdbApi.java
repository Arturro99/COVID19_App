package com.mobilki.covidapp.api;

import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.google.gson.Gson;
import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.api.repository.FilmRepository;
import com.mobilki.covidapp.exceptions.*;

import org.json.JSONException;
import org.json.JSONObject;

public class ImdbApi implements FilmDatabaseApi {

    private final String key = "da1a23f0cemsh550084e90351b5fp1c8b89jsn0e7a704c7bba";
    private final String host = "imdb8.p.rapidapi.com";
    private final OkHttpClient client = new OkHttpClient();

    private String jsonString;
    private static JSONObject jsonObject;
    private FilmRepository filmRepository = new FilmRepository();

    @Override
    public void getTitle(String endPoint, String id) throws JSONException {
        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/" + endPoint + "?tconst=" + id)
                .get()
                .addHeader("x-rapidapi-key", key)
                .addHeader("x-rapidapi-host", host)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                try {
                    throw new incorrectRequestException("Incorrect request.");
                } catch (incorrectRequestException ex) {
                    ex.printStackTrace();
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException{
                if (response.body() != null) {
                    jsonString = response.body().string();
                    try {
                        jsonObject = new JSONObject(jsonString);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                }
                else {
                    try {
                        throw new emptyResponseBodyException("Empty body in response.");
                    } catch (emptyResponseBodyException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    setTitle(id, jsonObject);
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public int getYearOfRelease(String endPoint, String id) {
        return 0;
    }

    @Override
    public String getRatings(String endPoint, String id) {
        return null;
    }

    @Override
    public String getDescription(String endPoint, String id) {
        return null;
    }

    @Override
    public List<String> getGenres(String endPoint, String id) {
        return null;
    }

    @Override
    public String getImageUrl(String endPoint, String id) {
        return null;
    }

    @Override
    public List<String> getActors(String endPoint, String id) {
        return null;
    }

    @Override
    public String getActorName(String endPoint, String id) {
        return null;
    }

    @Override
    public String getActorDateOfBirth(String endPoint, String id) {
        return null;
    }

    @Override
    public String getActorHeight(String endPoint, String id) {
        return null;
    }

    @Override
    public String getActorBirthPlace(String endPoint, String id) {
        return null;
    }

    private void setTitle(String id, JSONObject obj) throws JSONException, InterruptedException {
        filmRepository.addFilm(new Film(id));
        filmRepository.getFilm(id).setTitle(obj.getString("title"));
    }

    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }
}


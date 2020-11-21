package com.mobilki.covidapp.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
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

import org.json.JSONArray;
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
    public void fetchOverviewData(String id) {
        filmRepository.addFilm(new Film(id));
        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/title/get-overview-details" + "?tconst=" + id)
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

            @RequiresApi(api = Build.VERSION_CODES.R)
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
                    setReleaseYear(id, jsonObject);
                    setGenres(id, jsonObject);
                } catch (JSONException | InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
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
        filmRepository.getFilm(id).setTitle(
                obj.getJSONObject("title").getString("title"));
    }

    private void setDuration(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setYearOfRelease(
                obj.getJSONObject("title").getInt("runningTimeInMinutes"));
    }

    private void setReleaseYear(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setYearOfRelease(
                obj.getJSONObject("title").getInt("year"));
    }

    private void setImageUrl(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setImageUrl(
                obj.getJSONObject("title").getJSONObject("image").getString("url"));
    }

    private void setRatings(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setRatings(
                obj.getJSONObject("ratings").getDouble("rating"));
    }

    private void setRatingsCount(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setRatingsCount(
                obj.getJSONObject("ratings").getInt("ratingCount"));
    }

    private void setLongDescription(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setLongDescription(
                obj.getJSONObject("plotSummary").getString("text"));
    }

    private void setShortDescription(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setShortDescription(
                obj.getJSONObject("plotOutline").getString("text"));
    }

    @RequiresApi(api = Build.VERSION_CODES.R)
    private void setGenres(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setGenres(
                List.of(obj.getString("genres")));
    }

    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }
}


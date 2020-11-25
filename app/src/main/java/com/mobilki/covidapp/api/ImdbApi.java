package com.mobilki.covidapp.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;

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

public class ImdbApi implements FilmDatabaseApi, Runnable {

    private final String key = "da1a23f0cemsh550084e90351b5fp1c8b89jsn0e7a704c7bba";
    private final String host = "imdb8.p.rapidapi.com";
    private final OkHttpClient client = new OkHttpClient();

    private String jsonString;
    private static JSONObject jsonObject;
    private FilmRepository filmRepository = new FilmRepository();

    @Override
    public void fetchOverviewData(List<Film> films) {
        for (Film film : films) {
            String id = film.getId();
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
                public void onResponse(Call call, Response response) throws IOException {
                    if (response.body() != null) {
                        jsonString = response.body().string();
                        try {
                            jsonObject = new JSONObject(jsonString);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    } else {
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
                        setImageUrl(id, jsonObject);
                        setRatings(id, jsonObject);
                        setRatingsCount(id, jsonObject);
                        setDuration(id, jsonObject);
                        setShortDescription(id, jsonObject);
                        setLongDescription(id, jsonObject);
                    } catch (JSONException | InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            });
        }
    }

    @Override
    public void getMostPopularFilms() {
        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/title/get-most-popular-movies?purchaseCountry=US&homeCountry=US&currentCountry=US")
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
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    jsonString = response.body().string();
                } else {
                    try {
                        throw new emptyResponseBodyException("Empty body in response.");
                    } catch (emptyResponseBodyException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    instantiateFilms(jsonString, false);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getTopRatedFilms() {
        Request request = new Request.Builder()
                .url("https://imdb8.p.rapidapi.com/title/get-top-rated-movies")
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
            public void onResponse(Call call, Response response) throws IOException {
                if (response.body() != null) {
                    jsonString = response.body().string();
                } else {
                    try {
                        throw new emptyResponseBodyException("Empty body in response.");
                    } catch (emptyResponseBodyException e) {
                        e.printStackTrace();
                    }
                }
                try {
                    instantiateFilms(jsonString, true);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
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
    private void instantiateFilms(String jsonString, boolean ratedFilm) throws JSONException {
        List<String> ids = new ArrayList<>();
                ids = List.of(jsonString
                    .replace("[", "")
                    .replace("]", "")
                    .replace("\"", "")
                    .replace("/", "")
                    .replace("title", "")
                        .replace("{id:", "")
                    .split(","));

        for (int i = 0; i < 10; i++) {
            if (ratedFilm && i % 2 == 1)
                filmRepository.addFilm(new Film(ids.get(2*i)));
            else
                filmRepository.addFilm(new Film(ids.get(i)));
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)

    private void setGenres(String id, JSONObject obj) throws JSONException {
        filmRepository.getFilm(id).setGenres(
                List.of(obj.getString("genres")
                        .replace("[", "")
                        .replace("]", "")
                        .replace("\"", "")
                        .split(",")));
    }

    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }

    @Override
    public void run() {
        fetchOverviewData(getFilms());
    }
}


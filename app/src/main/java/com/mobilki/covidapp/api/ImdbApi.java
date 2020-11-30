package com.mobilki.covidapp.api;

import android.os.Build;
import android.text.format.DateFormat;
import android.util.Log;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.lang.reflect.Array;
import java.sql.Date;
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
import com.google.gson.JsonObject;
import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.api.repository.FilmGenresRepository;
import com.mobilki.covidapp.api.repository.FilmRepository;
import com.mobilki.covidapp.exceptions.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImdbApi implements FilmDatabaseApi {

    private final String key = "c13cb8428b25d1e30290182db543602c";
    private final String host = "imdb8.p.rapidapi.com";
    private final String imgFirstPartUrl = "https://image.tmdb.org/t/p/w500";
    private final OkHttpClient client = new OkHttpClient();

    private String jsonString;
    private static JSONObject jsonObject;
    private FilmRepository filmRepository = new FilmRepository();
    private FilmGenresRepository filmGenresRepository = new FilmGenresRepository();

    @Override
    public void getTopRatedOrPopularFilms(boolean topRated) {
        Request request = null;
        if (topRated) {
            request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/popular?api_key=" + key + "&language=en-US&page=1")
                    .get()
                    .build();
        }
        else {
            request = new Request.Builder()
                    .url("https://api.themoviedb.org/3/movie/top_rated?api_key=" + key + "&language=en-US&page=1")
                    .get()
                    .build();
        }
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
                    instantiateFilms(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    @Override
    public void getGenres() {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/genre/movie/list?api_key=" + key + "&language=en-US")
                .get()
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
                    setGenres(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void instantiateFilms(JSONObject obj) throws JSONException {
        for (int i = 0; i < 10; i++) {
            JSONObject o = (JSONObject) obj.getJSONArray("results").get(i);
            String id = o.getString("id");
            Film film = new Film(id);
            film.setTitle(o.getString("title"));
            film.setDateOfRelease(Date.valueOf(o.getString("release_date")));
            film.setRatingsCount(Integer.parseInt(o.getString("vote_count")));
            film.setRatings(Double.parseDouble(o.getString("vote_average")));
            film.setShortDescription(o.getString("overview"));
            film.setImageUrl(imgFirstPartUrl + o.getString("poster_path"));
            //film.setGenres(filmGenresRepository.getGenres().);

            filmRepository.addFilm(film);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)

    private void setGenres(JSONObject obj) throws JSONException {
        JSONArray arr = obj.getJSONArray("genres");
        for (int i = 0; i < arr.length(); i++) {
            filmGenresRepository.addGenre(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("name"));
        }
    }

    public List<Film> getFilms() {
        return filmRepository.getFilms();
    }

//    @Override
//    public void run() {
//        fetchOverviewData(getFilms());
//    }
}


package com.mobilki.covidapp.api;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.io.IOException;
import java.sql.Date;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import com.mobilki.covidapp.api.model.Actor;
import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.api.repository.FilmGenresRepository;
import com.mobilki.covidapp.api.repository.FilmRepository;
import com.mobilki.covidapp.exceptions.*;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ImdbApi implements EntertainmentDatabaseApi<Film, FilmSortingType> {

        private final String key = "c13cb8428b25d1e30290182db543602c";
    private final String host = "imdb8.p.rapidapi.com";
    private final String imgFirstPartUrl = "https://image.tmdb.org/t/p/w500";
    private final OkHttpClient client = new OkHttpClient();

    private String jsonString;
    private static JSONObject jsonObject;
    private FilmRepository filmRepository = new FilmRepository();
    private FilmGenresRepository filmGenresRepository = new FilmGenresRepository();

    @Override
    public void getSorted(FilmSortingType type, int number) {
        Request request = null;
        switch (type) {
            case TOP_RATED:
                request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/top_rated?api_key=" + key + "&language=en-US&page=1")
                        .get()
                        .build();
                break;
            case UPCOMING:
                request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/upcoming?api_key=" + key + "&language=en-US&page=1")
                        .get()
                        .build();
                break;
            case NOW_PLAYING:
                request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/now_playing?api_key=" + key + "&language=en-US&page=1")
                        .get()
                        .build();
                break;
            case MOST_POPULAR:
                request = new Request.Builder()
                        .url("https://api.themoviedb.org/3/movie/popular?api_key=" + key + "&language=en-US&page=1")
                        .get()
                        .build();
                break;
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
                    instantiateFilms(jsonObject, number);
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

    @Override
    public List<Film> getAll() {
        return filmRepository.getAll();
    }

    private void getCredits(String filmId) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + filmId + "/credits?api_key=" + key + "&language=en-US")
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
                    setCast(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void getDuration(String filmId) {
        Request request = new Request.Builder()
                .url("https://api.themoviedb.org/3/movie/" + filmId + "?api_key=" + key + "&language=en-US")
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
                    setDuration(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    @RequiresApi(api = Build.VERSION_CODES.R)
    private void instantiateFilms(JSONObject obj, int number) throws JSONException {
        for (int i = 0; i < number; i++) {
            JSONObject o = (JSONObject) obj.getJSONArray("results").get(i);
            String id = o.getString("id");
            Film film = new Film(id);

            getCredits(id);
            getDuration(id);

            film.setTitle(o.getString("title"));
            film.setDateOfRelease(Date.valueOf(o.getString("release_date")));
            film.setRatingsCount(Integer.parseInt(o.getString("vote_count")));
            film.setRatings(Double.parseDouble(o.getString("vote_average")));
            film.setShortDescription(o.getString("overview"));
            film.setImageUrl(imgFirstPartUrl + o.getString("poster_path"));

            List<Integer> arr = new ArrayList<>();
            JSONArray jArr = o.getJSONArray("genre_ids");
            for (int j = 0; j < jArr.length(); j++) {
                arr.add(jArr.getInt(j));
            }
            List<String> genres = new ArrayList<>();
            arr.stream()
                    .filter((filmGenresRepository.getGenres().keySet()::contains))
                    .collect(Collectors.toList())
                    .forEach(x -> genres.add(filmGenresRepository.getGenre(x)));
            film.setGenres(genres);

            filmRepository.add(film);
        }
    }


    @RequiresApi(api = Build.VERSION_CODES.R)

    private void setGenres(JSONObject obj) throws JSONException {
        JSONArray arr = obj.getJSONArray("genres");
        for (int i = 0; i < arr.length(); i++) {
           FilmGenresRepository.addGenre(arr.getJSONObject(i).getInt("id"), arr.getJSONObject(i).getString("name"));
        }
    }

    private void setCast(JSONObject obj) throws JSONException {
        JSONArray actorArr = obj.getJSONArray("cast");
        JSONArray directorArr = obj.getJSONArray("crew");
        for (int i = 0; i < actorArr.length(); i++) {
                Actor actor = new Actor(actorArr.getJSONObject(i).getInt("id"), actorArr.getJSONObject(i).getString("name"));
                actor.setImgUrl(imgFirstPartUrl +  actorArr.getJSONObject(i).getString("profile_path"));
                filmRepository.get(obj.getString("id"))
                        .addActor(actor);
        }
        for (int i = 0; i < directorArr.length(); i++) {
            if (directorArr.getJSONObject(i).getString("job").equals("Director")) {
                filmRepository.get(obj.getString("id"))
                        .addDirector(directorArr.getJSONObject(i).getInt("id"), directorArr.getJSONObject(i).getString("name"));
            }
        }
        if (actorArr.length() == 0) {
            filmRepository.get(obj.getString("id"))
                    .addActor(new Actor(0, "no data"));
        }
        if (directorArr.length() == 0) {
            filmRepository.get(obj.getString("id"))
                    .addDirector(0, "no data");
        }
    }

    private void setDuration(JSONObject obj) throws JSONException {
        filmRepository.get(obj.getString("id")).setDuration(obj.getInt("runtime"));
    }


//    public void run(boolean rated, int number) {
//        getTopRatedOrPopularFilms(rated, number);
//    }
}


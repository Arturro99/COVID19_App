package com.mobilki.covidapp.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.model.Book;
import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.api.repository.BookRepository;
import com.mobilki.covidapp.exceptions.emptyResponseBodyException;
import com.mobilki.covidapp.exceptions.incorrectRequestException;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.sql.Date;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class GoogleBooksApi {
    private final String imgFirstPartUrl = "https://www.googleapis.com/books/v1";

    private final OkHttpClient client = new OkHttpClient();

    private String jsonString;
    private static JSONObject jsonObject;
    private BookRepository bookRepository;

//    public void getDetails(String filmId) {
//        Request request = new Request.Builder()
//                .url(imgFirstPartUrl + "/volumes?q" + filmId)
//                .get()
//                .build();
//
//        client.newCall(request).enqueue(new Callback() {
//            @Override
//            public void onFailure(Call call, IOException e) {
//                try {
//                    throw new incorrectRequestException("Incorrect request.");
//                } catch (incorrectRequestException ex) {
//                    ex.printStackTrace();
//                }
//            }
//
//            @RequiresApi(api = Build.VERSION_CODES.R)
//            @Override
//            public void onResponse(Call call, Response response) throws IOException {
//                if (response.body() != null) {
//                    jsonString = response.body().string();
//                    try {
//                        jsonObject = new JSONObject(jsonString);
//                    } catch (JSONException e) {
//                        e.printStackTrace();
//                    }
//                } else {
//                    try {
//                        throw new emptyResponseBodyException("Empty body in response.");
//                    } catch (emptyResponseBodyException e) {
//                        e.printStackTrace();
//                    }
//                }
//                try {
//                    printLog(jsonObject);
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }

    public void getByGenre(String genre) {
        Request request = new Request.Builder()
                .url(imgFirstPartUrl + "/volumes?q=" + genre)
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
                    instantiateBook(jsonObject);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void instantiateBook(JSONObject jsonObject) throws JSONException {
        for (int i = 0; i < 10; i++) {
            JSONObject o = (JSONObject) jsonObject.getJSONArray("items").get(i);

            String id = o.getString("id");

            Book book = new Book(id);
            book.setTitle(o.getJSONObject("volumeInfo").getString("title"));
            book.setPublicationDate(Date.valueOf(o.getJSONObject("volumeInfo").getString("publishedDate")));
            book.setPublisher(o.getJSONObject("volumeInfo").getString("publisher"));
            book.setPages(o.getJSONObject("volumeInfo").getInt("pageCount"));
            book.setDescription(o.getString("description"));
            book.setImageUrl(o.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
            book.setPdfAvailable(o.getJSONObject("accessInfo").getJSONObject("pdf").getBoolean("isAvailable"));
            //TODO book.setAuthors()...
            //TODO book.setGenres()...
            //TODO ratings?
            bookRepository.add(book);
        }
    }

    public List<Book> getBooks() {
        return bookRepository.getAll();
    }
}

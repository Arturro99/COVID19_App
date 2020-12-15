package com.mobilki.covidapp.api;

import android.os.Build;
import android.util.Log;

import androidx.annotation.RequiresApi;

import com.mobilki.covidapp.api.model.Book;
import com.mobilki.covidapp.api.model.Film;
import com.mobilki.covidapp.api.repository.BookRepository;
import com.mobilki.covidapp.exceptions.emptyResponseBodyException;
import com.mobilki.covidapp.exceptions.incorrectRequestException;

import org.json.JSONArray;
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
    private BookRepository bookRepository = new BookRepository();

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

    public void getByGenre(String genre, int bookDigit) {
        Request request = new Request.Builder()
                .url(imgFirstPartUrl + "/volumes?q=subject:" + genre)
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
                    instantiateBook(jsonObject, bookDigit);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    public void instantiateBook(JSONObject jsonObject, int bookDigit) throws JSONException {
        for (int i = 0; i < bookDigit; i++) {
            JSONObject o = (JSONObject) jsonObject.getJSONArray("items").get(i);

            String id = o.getString("id");

            Book book = new Book(id);
            book.setTitle(o.getJSONObject("volumeInfo").getString("title"));
            JSONObject volumeInfo = o.getJSONObject("volumeInfo");
            if (volumeInfo.has("publishedDate")) {
                book.setPublicationDate(volumeInfo.getString("publishedDate"));
            }
            else {
                book.setPublicationDate("no data");
            }
            if (volumeInfo.has("averageRating")) {
                book.setRatings(volumeInfo.getString("averageRating"));
            }
            else {
                book.setRatings("no data");
            }
            if (volumeInfo.has("ratingsCount")) {
                book.setRatingsCount(volumeInfo.getString("ratingsCount"));
            }
            else {
                book.setRatingsCount("no data");
            }

            if (volumeInfo.has("publisher")) {
                book.setPublisher(volumeInfo.getString("publisher"));
            }
            else {
                book.setPublisher("no data");
            }

            if (volumeInfo.has("pageCount")) {
                book.setPages(volumeInfo.getString("pageCount"));
            }
            else {
                book.setPages("no data");
            }
            book.setDescription(o.getJSONObject("volumeInfo").getString("description"));
            book.setImageUrl(o.getJSONObject("volumeInfo").getJSONObject("imageLinks").getString("thumbnail"));
            book.setPdfAvailable(o.getJSONObject("accessInfo").getJSONObject("pdf").getBoolean("isAvailable"));

            if (volumeInfo.has("authors")) {
                JSONArray authors = o.getJSONObject("volumeInfo").getJSONArray("authors");
                for (int j = 0; j < authors.length(); j++) {
                    book.addAuthor((String) authors.get(j));
                }
            }
            else {
                book.addAuthor("no data");
            }
            if (volumeInfo.has("categories")) {
                JSONArray genres = o.getJSONObject("volumeInfo").getJSONArray("categories");
                for (int j = 0; j < genres.length(); j++) {
                    book.addGenre((String) genres.get(j));
                }
            }
            else {
                book.addGenre("no data");
            }
            bookRepository.add(book);
        }
    }

    public List<Book> getBooks() {
        return bookRepository.getAll();
    }
}

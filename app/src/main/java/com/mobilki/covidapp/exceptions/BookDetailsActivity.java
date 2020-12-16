package com.mobilki.covidapp.exceptions;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.model.Book;

public class BookDetailsActivity extends AppCompatActivity {

    private RatingBar ratingBar;
    private TextView title;
    private TextView ratingsCount;
    private TextView description;
    private TextView pdfAvailability;

    private Book book;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        book = (Book) getIntent().getSerializableExtra("book");

        ratingBar =  findViewById(R.id.bookRatingBar);
        title = findViewById(R.id.bookDetailedTitle);
        ratingsCount = findViewById(R.id.bookDetailedRatingsCount);
        description = findViewById(R.id.bookDetailedDescription);
        pdfAvailability = findViewById(R.id.pdfAvailable);

        if (!book.getRatings().equals("no data")) {
            ratingBar.setRating(Float.parseFloat(book.getRatings()));
        }
        else {
            ratingBar.setRating(0f);
        }
        title.setText(book.getTitle());
        if (!book.getRatingsCount().equals("no data")) {
            ratingsCount.setText(book.getRatingsCount());
        }
        else {
            ratingsCount.setText("???");
        }
        if (!book.getDescription().equals("no data")) {
            description.setText(book.getDescription());
        }
        else {
            ratingsCount.setText("no data");
        }
        pdfAvailability.setText(String.valueOf(book.isPdfAvailable()));

        description.setMovementMethod(new ScrollingMovementMethod());
    }
}
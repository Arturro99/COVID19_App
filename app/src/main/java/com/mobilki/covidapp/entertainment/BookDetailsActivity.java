package com.mobilki.covidapp.entertainment;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.widget.RatingBar;
import android.widget.TextView;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.model.Book;

public class BookDetailsActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

        Book book = (Book) getIntent().getSerializableExtra("book");

        RatingBar ratingBar = findViewById(R.id.bookRatingBar);
        TextView title = findViewById(R.id.bookDetailedTitle);
        TextView ratingsCount = findViewById(R.id.bookDetailedRatingsCount);
        TextView description = findViewById(R.id.bookDetailedDescription);
        TextView pdfAvailability = findViewById(R.id.pdfAvailable);

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
            ratingsCount.setText(R.string.no_data);
        }
        if (getResources().getConfiguration().locale.getLanguage().equals("en")) {
            pdfAvailability.setText(String.valueOf(book.isPdfAvailable()));
        }
        else {
            String available = book.isPdfAvailable() ? "tak" : "nie";
            pdfAvailability.setText(available);
        }

        description.setMovementMethod(new ScrollingMovementMethod());
    }
}
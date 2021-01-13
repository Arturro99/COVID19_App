package com.mobilki.covidapp.entertainment.fragments;

import android.annotation.SuppressLint;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.constraintlayout.widget.ConstraintSet;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.GoogleBooksApi;

public class BookFragment extends Fragment implements FragmentEntity{
    GoogleBooksApi googleBooksApi;
    LinearLayout booksLayout;
    int bookDigit;


    TextView[] bookTitleList;
    TextView[] bookGenresList;

    ImageButton[] bookPhotosList;

    TextView[] bookAuthorList;
    TextView[] bookAuthorTxtList;

    TextView[] bookPublicationDateList;
    TextView[] bookPublicationDateTxtList;

    TextView[] bookPagesList;
    TextView[] bookPagesTxtList;

    TextView[] bookRatingList;
    TextView[] bookRatingTxtList;

    ConstraintLayout[] bookConstraintLayoutList;

    public BookFragment() { super(R.layout.game_overview); }

    public BookFragment(LinearLayout layout, int filmDigit, GoogleBooksApi api) {
        this.booksLayout = layout;
        this.bookDigit = filmDigit;
        this.googleBooksApi = api;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeFields(bookDigit);
        setFields(bookDigit);
        fillFields(bookDigit);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initializeFields(int number) {
        bookTitleList = new TextView[number];
        bookGenresList = new TextView[number];

        bookPhotosList = new ImageButton[number];

        bookAuthorList = new TextView[number];
        bookAuthorTxtList = new TextView[number];

        bookPublicationDateList = new TextView[number];
        bookPublicationDateTxtList = new TextView[number];

        bookPagesList = new TextView[number];
        bookPagesTxtList = new TextView[number];

        bookRatingList = new TextView[number];
        bookRatingTxtList = new TextView[number];

        bookConstraintLayoutList = new ConstraintLayout[number];
    }

    @SuppressLint("InflateParams")
    @Override
    public void setFields(int number) {
        int bookTitleInitiateId = 11000;
        int bookPublicationDateInitiateId = 12000;
        int bookPublicationDateTxtInitiateId = 12500;
        int bookPhotoInitiateId = 13000;
        int bookAuthorInitiateId = 14000;
        int bookAuthorTxtInitiateId = 14500;
        int bookPagesInitiateId = 15000;
        int bookPagesTxtInitiateId = 15500;
        int bookRatingInitiateId = 16000;
        int bookRatingTxtInitiateId = 16500;
        int bookGenresInitiateId = 17000;
        int bookConstraintLayoutInitiateId = 20000;

        booksLayout.removeAllViews();
        for (int i = 0; i < bookDigit; i++) {
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.book_overview, null, true);
            bookConstraintLayoutList[i] = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.constraint, null);
            bookConstraintLayoutList[i].setId(bookConstraintLayoutInitiateId + i);
            linearLayout.addView(bookConstraintLayoutList[i]);

            bookTitleList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookTitleList[i].setId(bookTitleInitiateId + i);
            bookPhotosList[i] = (ImageButton) LayoutInflater.from(getContext()).inflate(R.layout.photo, null);
            bookPhotosList[i].setId(bookPhotoInitiateId + i);
            bookGenresList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookGenresList[i].setId(bookGenresInitiateId + i);
            bookPublicationDateList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookPublicationDateList[i].setId(bookPublicationDateInitiateId + i);
            bookPublicationDateTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookPublicationDateTxtList[i].setId(bookPublicationDateTxtInitiateId + i);
            bookAuthorList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookAuthorList[i].setId(bookAuthorInitiateId + i);
            bookAuthorTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookAuthorTxtList[i].setId(bookAuthorTxtInitiateId + i);
            bookPagesList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookPagesList[i].setId(bookPagesInitiateId + i);
            bookPagesTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookPagesTxtList[i].setId(bookPagesTxtInitiateId + i);
            bookRatingList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookRatingList[i].setId(bookRatingInitiateId + i);
            bookRatingTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            bookRatingTxtList[i].setId(bookRatingTxtInitiateId + i);

            bookConstraintLayoutList[i].addView(bookPhotosList[i], 0);
            bookConstraintLayoutList[i].addView(bookTitleList[i], 1);
            bookConstraintLayoutList[i].addView(bookAuthorTxtList[i], 2);
            bookConstraintLayoutList[i].addView(bookAuthorList[i], 3);
            bookConstraintLayoutList[i].addView(bookRatingTxtList[i], 4);
            bookConstraintLayoutList[i].addView(bookRatingList[i], 5);
            bookConstraintLayoutList[i].addView(bookPublicationDateTxtList[i], 6);
            bookConstraintLayoutList[i].addView(bookPublicationDateList[i], 7);
            bookConstraintLayoutList[i].addView(bookPagesTxtList[i], 8);
            bookConstraintLayoutList[i].addView(bookPagesList[i], 9);
            bookConstraintLayoutList[i].addView(bookGenresList[i], 10);

            booksLayout.addView(linearLayout);

            bookConstraintLayoutList[i].setMinWidth(1500);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(bookConstraintLayoutList[i]);

            //IMAGE
            constraintSet.connect(bookPhotosList[i].getId(), ConstraintSet.START, bookConstraintLayoutList[i].getId(), ConstraintSet.START);
            constraintSet.connect(bookPhotosList[i].getId(), ConstraintSet.TOP, bookConstraintLayoutList[i].getId(), ConstraintSet.TOP);

            //TITLE
            constraintSet.connect(bookTitleList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookTitleList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookTitleList[i].getId(), ConstraintSet.TOP, bookConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            bookTitleList[i].setTextSize(15);
            bookTitleList[i].setTypeface(Typeface.DEFAULT_BOLD);


            //AUTHOR TXT
            constraintSet.connect(bookAuthorTxtList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookAuthorTxtList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookAuthorTxtList[i].getId(), ConstraintSet.START, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookAuthorTxtList[i].getId(), ConstraintSet.TOP, bookConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookAuthorTxtList[i].getId(), 0.15f);

            //AUTHOR
            constraintSet.connect(bookAuthorList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookAuthorList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookAuthorList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookAuthorList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.setVerticalBias(bookAuthorList[i].getId(), 0.01f);

            //PUBLICATION DATE TXT
            constraintSet.connect(bookPublicationDateTxtList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookPublicationDateTxtList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPublicationDateTxtList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPublicationDateTxtList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPublicationDateTxtList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(bookPublicationDateTxtList[i].getId(), 0.1f);

            //PUBLICATION DATE
            constraintSet.connect(bookPublicationDateList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookPublicationDateList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPublicationDateList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPublicationDateList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPublicationDateList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(bookPublicationDateList[i].getId(), 0.6f);

            //PAGES TXT
            constraintSet.connect(bookPagesTxtList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookPagesTxtList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPagesTxtList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPagesTxtList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPagesTxtList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(bookPagesTxtList[i].getId(), 0.1f);

            //PAGES
            constraintSet.connect(bookPagesList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookPagesList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPagesList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookPagesList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookPagesList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(bookPagesList[i].getId(), 0.6f);

            //RATINGS TXT
            constraintSet.connect(bookRatingTxtList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookRatingTxtList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookRatingTxtList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookRatingTxtList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookRatingTxtList[i].getId(), 0.6f);
            constraintSet.setHorizontalBias(bookRatingTxtList[i].getId(), 0.1f);

            //RATINGS
            constraintSet.connect(bookRatingList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookRatingList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookRatingList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookRatingList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookRatingList[i].getId(), 0.6f);
            constraintSet.setHorizontalBias(bookRatingList[i].getId(), 0.6f);

            //Genres
            constraintSet.connect(bookGenresList[i].getId(), ConstraintSet.BOTTOM, bookConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(bookGenresList[i].getId(), ConstraintSet.END, bookConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookGenresList[i].getId(), ConstraintSet.START, bookPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(bookGenresList[i].getId(), ConstraintSet.TOP, bookPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(bookGenresList[i].getId(), 0.8f);
            constraintSet.setHorizontalBias(bookGenresList[i].getId(), 0.1f);

            constraintSet.applyTo(bookConstraintLayoutList[i]);
        }
    }

    @Override
    public void fillFields(int number) {
        for (int i = 0; i < number; i++) {
            bookTitleList[i].setText(googleBooksApi.getAll().get(i).getTitle());
            bookPublicationDateList[i].setText(String.valueOf(googleBooksApi.getAll().get(i).getPublicationDate()));
            bookPagesList[i].setText(String.valueOf(googleBooksApi.getAll().get(i).getPages()));
            bookRatingList[i].setText(String.valueOf(googleBooksApi.getAll().get(i).getRatings()));
            bookAuthorList[i].setText(String.valueOf(googleBooksApi.getAll().get(i).getAuthors())
                    .replace("[", "")
                    .replace("]", ""));
            bookGenresList[i].setText(String.valueOf(googleBooksApi.getAll().get(i).getGenres())
                    .replace("[", "")
                    .replace("]", "")
                    .replace(", ", "\n"));
            Glide.with(this).load(googleBooksApi.getAll().get(i).getImageUrl()).placeholder(R.drawable.placeholder).into(bookPhotosList[i]);

            bookAuthorTxtList[i].setText(R.string.author);
            bookPublicationDateTxtList[i].setText(R.string.released);
            bookPagesTxtList[i].setText(R.string.pages);
            bookRatingTxtList[i].setText(R.string.ratings);
        }
    }

    @Override
    public ImageButton getPhoto(int index) {
        return bookPhotosList[index];
    }
}

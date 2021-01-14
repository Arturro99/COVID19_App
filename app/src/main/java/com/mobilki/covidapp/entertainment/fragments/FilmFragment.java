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

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.ImdbApi;
import com.squareup.picasso.Picasso;

public class FilmFragment extends Fragment implements FragmentEntity{
    ImdbApi imdbApi;
    LinearLayout filmsLayout;
    int filmDigit;

    TextView[] filmTitleList;
    TextView[] filmGenresList;

    ImageButton[] filmPhotosList;

    TextView[] filmDirectorList;
    TextView[] filmDirectorTxtList;

    TextView[] filmReleaseYearList;
    TextView[] filmReleaseYearTxtList;

    TextView[] filmDurationList;
    TextView[] filmDurationTxtList;

    TextView[] filmRatingList;
    TextView[] filmRatingTxtList;

    ConstraintLayout[] filmConstraintLayoutList;

    public FilmFragment() { super(R.layout.film_overview); }

    public FilmFragment(LinearLayout layout, int filmDigit, ImdbApi api) {
        this.filmsLayout = layout;
        this.filmDigit = filmDigit;
        this.imdbApi = api;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.d("TAG", "onCreateView: FILM");
        initializeFields(filmDigit);
        setFields(filmDigit);
        fillFields(filmDigit);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initializeFields(int number) {
        filmTitleList = new TextView[number];
        filmGenresList = new TextView[number];

        filmPhotosList = new ImageButton[number];

        filmDirectorList = new TextView[number];
        filmDirectorTxtList = new TextView[number];

        filmReleaseYearList = new TextView[number];
        filmReleaseYearTxtList = new TextView[number];

        filmDurationList = new TextView[number];
        filmDurationTxtList = new TextView[number];

        filmRatingList = new TextView[number];
        filmRatingTxtList = new TextView[number];

        filmConstraintLayoutList = new ConstraintLayout[number];
    }

    @SuppressLint("InflateParams")
    @Override
    public void setFields(int filmDigit) {
        int filmTitleInitiateId = 1000;
        int filmReleaseYearInitiateId = 2000;
        int filmReleaseYearTxtInitiateId = 2500;
        int filmPhotoInitiateId = 3000;
        int filmDirectorInitiateId = 4000;
        int filmDirectorTxtInitiateId = 4500;
        int filmDurationInitiateId = 5000;
        int filmDurationTxtInitiateId = 5500;
        int filmRatingInitiateId = 6000;
        int filmRatingTxtInitiateId = 6500;
        int filmGenresInitiateId = 7000;
        int filmConstraintLayoutInitiateId = 10000;

        filmsLayout.removeAllViews();
        for (int i = 0; i < filmDigit; i++) {
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.film_overview, null);

            filmConstraintLayoutList[i] = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.constraint, null);
            filmConstraintLayoutList[i].setId(filmConstraintLayoutInitiateId + i);
            linearLayout.addView(filmConstraintLayoutList[i]);

            filmTitleList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmTitleList[i].setId(filmTitleInitiateId + i);
            filmPhotosList[i] = (ImageButton) LayoutInflater.from(getContext()).inflate(R.layout.photo, null);
            filmPhotosList[i].setId(filmPhotoInitiateId + i);
            filmGenresList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmGenresList[i].setId(filmGenresInitiateId + i);
            filmDirectorList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmDirectorList[i].setId(filmDirectorInitiateId + i);
//            filmDirectorTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
//            filmDirectorTxtList[i].setId(filmDirectorTxtInitiateId + i);
            filmRatingList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmRatingList[i].setId(filmRatingInitiateId + i);
            filmRatingTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmRatingTxtList[i].setId(filmRatingTxtInitiateId + i);
            filmReleaseYearList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmReleaseYearList[i].setId(filmReleaseYearInitiateId + i);
            filmReleaseYearTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmReleaseYearTxtList[i].setId(filmReleaseYearTxtInitiateId + i);
            filmDurationList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmDurationList[i].setId(filmDurationInitiateId + i);
            filmDurationTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            filmDurationTxtList[i].setId(filmDurationTxtInitiateId + i);

            filmConstraintLayoutList[i].addView(filmPhotosList[i], 0);
            filmConstraintLayoutList[i].addView(filmTitleList[i], 1);
//            filmConstraintLayoutList[i].addView(filmDirectorTxtList[i], 2);
            filmConstraintLayoutList[i].addView(filmDirectorList[i], 2);
            filmConstraintLayoutList[i].addView(filmRatingTxtList[i], 3);
            filmConstraintLayoutList[i].addView(filmRatingList[i], 4);
            filmConstraintLayoutList[i].addView(filmReleaseYearTxtList[i], 5);
            filmConstraintLayoutList[i].addView(filmReleaseYearList[i], 6);
            filmConstraintLayoutList[i].addView(filmDurationTxtList[i], 7);
            filmConstraintLayoutList[i].addView(filmDurationList[i], 8);
            filmConstraintLayoutList[i].addView(filmGenresList[i], 9);

            filmsLayout.addView(linearLayout);

            filmConstraintLayoutList[i].setMinWidth(1500);
            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(filmConstraintLayoutList[i]);

            //IMAGE
            constraintSet.connect(filmPhotosList[i].getId(), ConstraintSet.START, filmConstraintLayoutList[i].getId(), ConstraintSet.START);
            constraintSet.connect(filmPhotosList[i].getId(), ConstraintSet.TOP, filmConstraintLayoutList[i].getId(), ConstraintSet.TOP);

            //TITLE
            constraintSet.connect(filmTitleList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmTitleList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmTitleList[i].getId(), ConstraintSet.TOP, filmConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            filmTitleList[i].setTextSize(15);
            filmTitleList[i].setTypeface(Typeface.DEFAULT_BOLD);

            //DIRECTOR TXT
//            constraintSet.connect(filmDirectorTxtList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
//            constraintSet.connect(filmDirectorTxtList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
//            constraintSet.connect(filmDirectorTxtList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
//            constraintSet.connect(filmDirectorTxtList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
//            constraintSet.setVerticalBias(filmDirectorTxtList[i].getId(), 0.15f);

            //DIRECTOR
            constraintSet.connect(filmDirectorList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmDirectorList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDirectorList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDirectorList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDirectorList[i].getId(), 0.15f);

            //RELEASE YEAR TXT
            constraintSet.connect(filmReleaseYearTxtList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmReleaseYearTxtList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmReleaseYearTxtList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmReleaseYearTxtList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmReleaseYearTxtList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(filmReleaseYearTxtList[i].getId(), 0.1f);

            //RELEASE YEAR
            constraintSet.connect(filmReleaseYearList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmReleaseYearList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmReleaseYearList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmReleaseYearList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmReleaseYearList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(filmReleaseYearList[i].getId(), 0.6f);

            //DURATION TXT
            constraintSet.connect(filmDurationTxtList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmDurationTxtList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDurationTxtList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDurationTxtList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDurationTxtList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(filmDurationTxtList[i].getId(), 0.1f);

            //DURATION
            constraintSet.connect(filmDurationList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmDurationList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDurationList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmDurationList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmDurationList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(filmDurationList[i].getId(), 0.6f);

            //RATINGS TXT
            constraintSet.connect(filmRatingTxtList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmRatingTxtList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmRatingTxtList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmRatingTxtList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmRatingTxtList[i].getId(), 0.6f);
            constraintSet.setHorizontalBias(filmRatingTxtList[i].getId(), 0.1f);

            //RATINGS
            constraintSet.connect(filmRatingList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmRatingList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmRatingList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmRatingList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmRatingList[i].getId(), 0.6f);
            constraintSet.setHorizontalBias(filmRatingList[i].getId(), 0.6f);

            //Genres
            constraintSet.connect(filmGenresList[i].getId(), ConstraintSet.BOTTOM, filmConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(filmGenresList[i].getId(), ConstraintSet.END, filmConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmGenresList[i].getId(), ConstraintSet.START, filmPhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(filmGenresList[i].getId(), ConstraintSet.TOP, filmPhotosList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(filmGenresList[i].getId(), 0.8f);
            constraintSet.setHorizontalBias(filmGenresList[i].getId(), 0.1f);

            constraintSet.applyTo(filmConstraintLayoutList[i]);
        }
    }

    @Override
    public void fillFields(int number) {
        for (int i = 0; i < number; i++) {
            filmTitleList[i].setText(imdbApi.getAll().get(i).getTitle());
            filmReleaseYearList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDateOfRelease()));
            filmDurationList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDuration()));
            filmRatingList[i].setText(String.valueOf(imdbApi.getAll().get(i).getRatings()));
            filmGenresList[i].setText(String.valueOf(imdbApi.getAll().get(i).getGenres())
                    .replace("[", "")
                    .replace("]", ""));
            filmDirectorList[i].setText(String.valueOf(imdbApi.getAll().get(i).getDirectors().values())
                    .replace("[", "")
                    .replace("]", ""));
            Picasso.get().load(imdbApi.getAll().get(i).getImageUrl()).placeholder(R.drawable.placeholder).into(filmPhotosList[i]);

//            filmDirectorTxtList[i].setText(R.string.director);
            filmReleaseYearTxtList[i].setText(R.string.released);
            filmDurationTxtList[i].setText(R.string.duration);
            filmRatingTxtList[i].setText(R.string.ratings);
        }
    }

    @Override
    public ImageButton getPhoto(int index) { return filmPhotosList[index]; }

}

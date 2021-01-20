package com.mobilki.covidapp.entertainment.fragments;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.DisplayMetrics;
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
import com.mobilki.covidapp.api.repository.GameRepository;

import java.util.Locale;

public class GameFragment extends Fragment implements FragmentEntity{
    LinearLayout gamesLayout;
    GameRepository gameRepository;
    TextView[] gameTitleList;
    TextView[] gameGenresList;

    ImageButton[] gamePhotosList;

    TextView[] gamePlayersList;
    TextView[] gamePlayersTxtList;

    TextView[] gameAgeList;
    TextView[] gameAgeTxtList;

    TextView[] gameTimeList;
    TextView[] gameTimeTxtList;

    ConstraintLayout[] gameConstraintLayoutList;
    public GameFragment() { super(R.layout.game_overview); }

    public GameFragment(LinearLayout layout, GameRepository gameRepository) {
        this.gamesLayout = layout;
        this.gameRepository = gameRepository;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        initializeFields(8);
        setFields(8);
        fillFields(8);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void initializeFields(int bookDigit) {
        gameTitleList = new TextView[bookDigit];
        gameGenresList = new TextView[bookDigit];

        gamePhotosList = new ImageButton[bookDigit];

        gameAgeList = new TextView[bookDigit];
        gameAgeTxtList = new TextView[bookDigit];

        gamePlayersList = new TextView[bookDigit];
        gamePlayersTxtList = new TextView[bookDigit];

        gameTimeList = new TextView[bookDigit];
        gameTimeTxtList = new TextView[bookDigit];

        gameConstraintLayoutList = new ConstraintLayout[8];
    }

    @SuppressLint("InflateParams")
    @Override
    public void setFields(int bookDigit) {
        int gameTitleInitiateId = 21000;
        int gameAgeInitiateId = 22000;
        int gameAgeTxtInitiateId = 22500;
        int gamePhotoInitiateId = 23000;
        int gamePlayersInitiateId = 24000;
        int gamePlayersTxtInitiateId = 24500;
        int gameTimeInitiateId = 25000;
        int gameTimeTxtInitiateId = 25500;
        int gameGenresInitiateId = 26000;
        int gameConstraintLayoutInitiateId = 30000;

        gamesLayout.removeAllViews();
        for (int i = 0; i < bookDigit; i++) {
            LinearLayout linearLayout = (LinearLayout) getLayoutInflater().inflate(R.layout.game_overview, null);

            gameConstraintLayoutList[i] = (ConstraintLayout) LayoutInflater.from(getContext()).inflate(R.layout.constraint, null);
            gameConstraintLayoutList[i].setId(gameConstraintLayoutInitiateId + i);
            linearLayout.addView(gameConstraintLayoutList[i]);

            gameTitleList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameTitleList[i].setId(gameTitleInitiateId + i);
            gamePhotosList[i] = (ImageButton) LayoutInflater.from(getContext()).inflate(R.layout.photo, null);
            gamePhotosList[i].setId(gamePhotoInitiateId + i);
            gameGenresList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameGenresList[i].setId(gameGenresInitiateId + i);
            gameAgeList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameAgeList[i].setId(gameAgeInitiateId + i);
            gameAgeTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameAgeTxtList[i].setId(gameAgeTxtInitiateId + i);
            gamePlayersList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gamePlayersList[i].setId(gamePlayersInitiateId + i);
            gamePlayersTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gamePlayersTxtList[i].setId(gamePlayersTxtInitiateId + i);
            gameTimeList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameTimeList[i].setId(gameTimeInitiateId + i);
            gameTimeTxtList[i] = (TextView) LayoutInflater.from(getContext()).inflate(R.layout.text, null);
            gameTimeTxtList[i].setId(gameTimeTxtInitiateId + i);

            gameConstraintLayoutList[i].addView(gamePhotosList[i], 0);
            gameConstraintLayoutList[i].addView(gameTitleList[i], 1);
            gameConstraintLayoutList[i].addView(gameAgeTxtList[i], 2);
            gameConstraintLayoutList[i].addView(gameAgeList[i], 3);
            gameConstraintLayoutList[i].addView(gamePlayersTxtList[i], 4);
            gameConstraintLayoutList[i].addView(gamePlayersList[i], 5);
            gameConstraintLayoutList[i].addView(gameTimeTxtList[i], 6);
            gameConstraintLayoutList[i].addView(gameTimeList[i], 7);
            gameConstraintLayoutList[i].addView(gameGenresList[i], 8);

            gamesLayout.addView(linearLayout);



            ConstraintSet constraintSet = new ConstraintSet();
            constraintSet.clone(gameConstraintLayoutList[i]);

            DisplayMetrics displayMetrics = new DisplayMetrics();
            ((Activity)getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
            int width = displayMetrics.widthPixels;

            gameConstraintLayoutList[i].setMinWidth((int) Math.round(width*1.5));

            //IMAGE
            constraintSet.connect(gamePhotosList[i].getId(), ConstraintSet.START, gameConstraintLayoutList[i].getId(), ConstraintSet.START);
            constraintSet.connect(gamePhotosList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            gamePhotosList[i].setMaxWidth(width/2);

            //TITLE
            constraintSet.connect(gameTitleList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTitleList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTitleList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            gameTitleList[i].setTextSize(15);
            gameTitleList[i].setTypeface(Typeface.DEFAULT_BOLD);


            //AGE TXT
            constraintSet.connect(gameAgeTxtList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gameAgeTxtList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameAgeTxtList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameAgeTxtList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameAgeTxtList[i].getId(), 0.3f);
            constraintSet.setHorizontalBias(gameAgeTxtList[i].getId(), 0.1f);

            //AGE
            constraintSet.connect(gameAgeList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gameAgeList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameAgeList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameAgeList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameAgeList[i].getId(), 0.3f);
            constraintSet.setHorizontalBias(gameAgeList[i].getId(), 0.6f);

            //Players TXT
            constraintSet.connect(gamePlayersTxtList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gamePlayersTxtList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gamePlayersTxtList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gamePlayersTxtList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gamePlayersTxtList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(gamePlayersTxtList[i].getId(), 0.1f);

            //Players
            constraintSet.connect(gamePlayersList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gamePlayersList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gamePlayersList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gamePlayersList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gamePlayersList[i].getId(), 0.4f);
            constraintSet.setHorizontalBias(gamePlayersList[i].getId(), 0.6f);

            //TIME TXT
            constraintSet.connect(gameTimeTxtList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gameTimeTxtList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTimeTxtList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTimeTxtList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameTimeTxtList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(gameTimeTxtList[i].getId(), 0.1f);

            //TIME
            constraintSet.connect(gameTimeList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gameTimeList[i].getId(), ConstraintSet.END, gameConstraintLayoutList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTimeList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameTimeList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameTimeList[i].getId(), 0.5f);
            constraintSet.setHorizontalBias(gameTimeList[i].getId(), 0.6f);

            //Genres
            constraintSet.connect(gameGenresList[i].getId(), ConstraintSet.BOTTOM, gameConstraintLayoutList[i].getId(), ConstraintSet.BOTTOM);
            constraintSet.connect(gameGenresList[i].getId(), ConstraintSet.START, gamePhotosList[i].getId(), ConstraintSet.END);
            constraintSet.connect(gameGenresList[i].getId(), ConstraintSet.TOP, gameConstraintLayoutList[i].getId(), ConstraintSet.TOP);
            constraintSet.setVerticalBias(gameGenresList[i].getId(), 0.8f);

            constraintSet.applyTo(gameConstraintLayoutList[i]);
        }
    }

    @Override
    public void fillFields(int bookDigit) {
        for (int i = 0; i < bookDigit; i++) {
            gameTitleList[i].setText(gameRepository.getAll().get(i).getTitleEn());
            gameAgeList[i].setText(String.format(Locale.US,"%d-%d", gameRepository.getAll().get(i).getAgeMin(), gameRepository.getAll().get(i).getAgeMax()));
            gamePlayersList[i].setText(gameRepository.getAll().get(i).getPlayersMin() == gameRepository.getAll().get(i).getPlayersMax() ?
                    String.valueOf(gameRepository.getAll().get(i).getPlayersMin()) :
                    gameRepository.getAll().get(i).getPlayersMin() + "-" + gameRepository.getAll().get(i).getPlayersMax());
            gameTimeList[i].setText(String.format(Locale.US,"%s minutes", gameRepository.getAll().get(i).getTime()));
            gameGenresList[i].setText(String.valueOf(gameRepository.getAll().get(i).getGenreEn())
                    .replace(", ", "\n"));
            Glide.with(this).load(gameRepository.getAll().get(i).getImgLink()).placeholder(R.drawable.placeholder).into(gamePhotosList[i]);

            gameAgeTxtList[i].setText(R.string.age);
            gamePlayersTxtList[i].setText(R.string.players);
            gameTimeTxtList[i].setText(R.string.time);
        }
    }

    @Override
    public ImageButton getPhoto(int index) {
        return gamePhotosList[index];
    }
}

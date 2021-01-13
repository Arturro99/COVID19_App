package com.mobilki.covidapp.entertainment.fragments;

import android.widget.ImageButton;

public interface FragmentEntity {
    void initializeFields(int number);
    void setFields(int number);
    void fillFields(int number);
    ImageButton getPhoto(int index);
}

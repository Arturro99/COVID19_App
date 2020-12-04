package com.mobilki.covidapp.api.imageslistview;

import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.mobilki.covidapp.R;

public class MyHolder {
    TextView actorName;
    ImageView actorImg;

    public MyHolder(View v) {
        actorImg = (ImageView) v.findViewById(R.id.actorImg);
        actorName = (TextView) v.findViewById(R.id.actorName);
    }
}

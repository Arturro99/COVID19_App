package com.mobilki.covidapp.api.imageslistview;

import android.content.Context;
import android.widget.ImageView;

import com.mobilki.covidapp.R;
import com.squareup.picasso.Picasso;

public class PicassoClient {
    public static void downloadImage(String url, ImageView img) {
        if (url != null && url.length() > 0) {
            Picasso.get().load(url).placeholder(R.drawable.placeholder).into(img);
        }
    }
}

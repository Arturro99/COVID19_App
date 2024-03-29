package com.mobilki.covidapp.api.imageslistview;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.mobilki.covidapp.R;
import com.mobilki.covidapp.api.model.Actor;

import java.util.ArrayList;

public class CustomAdapter extends BaseAdapter {

    Context c;
    ArrayList<Actor> actors;
    LayoutInflater inflater;
    SharedPreferences settings;

    public CustomAdapter(Context c, ArrayList<Actor> actors) {
        this.c = c;
        this.actors = actors;
    }

    @Override
    public int getCount() {
        return actors.size();
    }

    @Override
    public Object getItem(int i) {
        return actors.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (inflater == null)
            inflater = (LayoutInflater) c.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (view == null)
            view = inflater.inflate(R.layout.list_item, viewGroup, false);

        MyHolder holder = new MyHolder(view);
        holder.actorName.setText(actors.get(i).getName());
        settings = c.getSharedPreferences(c.getResources().getString(R.string.shared_preferences),0);
        holder.actorName.setTextColor(settings.getBoolean("darkModeOn", false) ? c.getResources().getColor(R.color.white) : c.getResources().getColor(R.color.black));

        PicassoClient.downloadImage(actors.get(i).getImgUrl(), holder.actorImg);

        return view;
    }
}

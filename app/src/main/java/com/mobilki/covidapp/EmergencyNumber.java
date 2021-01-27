package com.mobilki.covidapp;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class EmergencyNumber extends AppCompatActivity {
    Button _112;
    Button ems;
    Button fd;
    Button police;
    Button sanepid;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_numbers);
        _112 = findViewById(R.id.button112);
        ems = findViewById(R.id.button999);
        fd = findViewById(R.id.button998);
        police = findViewById(R.id.button997);
        sanepid = findViewById(R.id.buttonSanepid);

        setUpBtns();
    }

    private void setUpBtns() {
        setNumber(_112, getString(R.string.number_112));
        setNumber(ems, getString(R.string.number_999));
        setNumber(fd, getString(R.string.number_998));
        setNumber(police, getString(R.string.number_997));
        setNumber(sanepid, getString(R.string.number_sanepid));
    }

    private void setNumber(Button btn, String number) {
        btn.setOnClickListener(view -> {
            Uri nr = Uri.parse(number);
            Intent callIntent = new Intent(Intent.ACTION_DIAL, nr);
            startActivity(callIntent);
        });
    }
}

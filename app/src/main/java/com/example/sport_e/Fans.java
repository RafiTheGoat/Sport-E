package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class Fans extends AppCompatActivity {

    private ImageView fback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);

        fback = (ImageView) findViewById(R.id.fback);

        fback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fback_intent = new Intent(Fans.this, Home.class);
                startActivity(fback_intent);
            }
        });
    }
}
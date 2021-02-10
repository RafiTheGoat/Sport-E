package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class Predict extends AppCompatActivity {

    private ImageView pback;
    private Button hi,hd,ai,ad,place;
    private TextView home,away;
    int hscore = 0;
    int awscore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        hi = findViewById(R.id.hsi);
        hd = findViewById(R.id.hsd);
        ai = findViewById(R.id.asi);
        ad = findViewById(R.id.asd);
        home = findViewById(R.id.predict_hometeam_score);
        away = findViewById(R.id.predict_awayteam_score);
        place = findViewById(R.id.placeprediction_btn);



        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hscore++;
                home.setText(String.valueOf(hscore));


            }
        });
        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hscore > 0) {
                    hscore--;
                    home.setText(String.valueOf(hscore));
                }
            }
        });
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    awscore++;
                    away.setText(String.valueOf(awscore));
                }

        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awscore > 0) {
                    awscore--;
                    away.setText(String.valueOf(awscore));
                }
            }
        });
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Predict.this, "Prediction Is placed!!", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(Predict.this,Home.class);
                startActivity(intent);
            }
        });

        pback = (ImageView) findViewById(R.id.pback);

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pback_intent = new Intent(Predict.this, Home.class);
                startActivity(pback_intent);
            }
        });
    }
}
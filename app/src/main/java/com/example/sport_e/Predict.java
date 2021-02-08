package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

public class Predict extends AppCompatActivity {

    private ImageView pback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);

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
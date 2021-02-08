package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.ImageView;

public class Brigade extends AppCompatActivity {

    private ImageView bback;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigade);

        bback = (ImageView) findViewById(R.id.bback);

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bback_intent = new Intent(Brigade.this, Home.class);
                startActivity(bback_intent);
            }
        });
    }
}
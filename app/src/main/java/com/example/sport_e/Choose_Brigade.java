package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.lang.reflect.Member;

import static android.widget.AdapterView.*;

public class Choose_Brigade extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private FirebaseAuth mAuth;
    Button finalise;

    Spinner team;

   // private TextView display_team;
    DatabaseReference dbr;
    String item;
    team Team;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose__brigade);
        getSupportActionBar().hide();

        mAuth = FirebaseAuth.getInstance();
        Intent intent = getIntent();
        String name = intent.getStringExtra("name");
        String email = intent.getStringExtra("email");
        String pass = intent.getStringExtra("pass");

        String[] team_items = getResources().getStringArray(R.array.Teams);
        team = (Spinner) findViewById(R.id.team_spinner);

        FirebaseDatabase db = FirebaseDatabase.getInstance();
        dbr = db.getReference("Team");
        team.setOnItemSelectedListener(this);


        team Team = new team();
        ArrayAdapter arrayAdapter = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, team_items);
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        team.setAdapter(arrayAdapter);


        finalise = (Button) findViewById(R.id.finalise_btn);

       // display_team = (TextView) findViewById(R.id.team);


        finalise.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent home = new Intent(Choose_Brigade.this, Registration.class);
                home.putExtra("team",item);
                startActivity(home);

              //  SaveValue(item);

            }

        });
    }


    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        item = team.getSelectedItem().toString();
       // display_team.setText(item);

    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {

    }
/*
    public void SaveValue(String item) {
        if (item == "") {
            Toast.makeText(this, "Please select a team", Toast.LENGTH_LONG).show();
        } else {
            Team.setTeam_name(item);
            String id = dbr.push().getKey();
            dbr.child(id).setValue(Team);
            Toast.makeText(this, "Team is selected", Toast.LENGTH_SHORT).show();
            Intent home = new Intent(Choose_Brigade.this, MainActivity.class);
            startActivity(home);

        }
    }
    */
}
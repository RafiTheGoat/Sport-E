package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class Profile extends AppCompatActivity {

    private ImageView proback;
    private Button logout;
    private TextView user,uname,sTeam;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        proback = (ImageView) findViewById(R.id.proback);
        logout = (Button) findViewById(R.id.logout);
        user = findViewById(R.id.profile_name);
        uname = findViewById(R.id.Name);
        sTeam = findViewById(R.id.sTeam);

        proback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent proback_intent = new Intent(Profile.this, Home.class);
                startActivity(proback_intent);
            }
        });

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(Profile.this,MainActivity.class));
            }
        });
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();
        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    String name = userProfile.name;
                    String email = userProfile.email_id;
                    String team = userProfile.team;

                    user.setText(email);
                    uname.setText(name);
                    sTeam.setText(team);

                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Profile.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }
}
package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class leaderboard extends AppCompatActivity {

    TextView yourScore;
    List<String> jNames = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leaderboard);


        yourScore = findViewById(R.id.lead);
        recyclerView = findViewById(R.id.leaderboardFriends);

        String uid = FirebaseAuth.getInstance().getUid();

        DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String points = snapshot.child("points").getValue().toString();
                yourScore.setText("YOUR SCORE:    "+points);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"error loading data", Toast.LENGTH_SHORT).show();
            }
        });

        initRecyclerView();
        jNames.clear();

        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(uid).child("Friends");

        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                for (DataSnapshot dsp : snapshot.getChildren()) {

                    String FriendUId = dsp.getKey();

                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(FriendUId);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            String points = snapshot.child("points").getValue().toString();
                            String name = snapshot.child("name").getValue().toString();

                            String temp = name + "____" + points;

                            jNames.add(temp);
                            recyclerView.getAdapter().notifyItemInserted(jNames.size());

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "error loading data", Toast.LENGTH_SHORT).show();
                        }
                    });


                }


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Loading data", Toast.LENGTH_SHORT).show();
            }
        });


    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new leaderBoardadapter(jNames, this));
    }
}
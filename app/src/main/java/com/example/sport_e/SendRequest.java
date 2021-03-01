package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.NumberPicker;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class SendRequest extends AppCompatActivity {

    EditText SearchResult;
    Button SearchButton,leader;
    DatabaseReference mDatabase;
    long f;
    String name;
    List<String> jNames = new ArrayList<>();
    RecyclerView recyclerView;
    LinearLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_send_request);

        recyclerView = findViewById(R.id.pendingRecycler);

        mDatabase = FirebaseDatabase.getInstance().getReference();
        SearchResult = findViewById(R.id.search);
        SearchButton = findViewById(R.id.searchbtn);
        leader = findViewById(R.id.leader);

        leader.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent leader = new Intent(SendRequest.this, leaderboard.class);
                startActivity(leader);
            }
        });

        SearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String email = SearchResult.getText().toString();

                if(email.length()==0)
                {
                    SearchResult.setError("Please Enter Email Id");
                }
                else
                {
                    f = 0;
                    final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    final DatabaseReference ref = mDatabase.child("Users");

                    ref.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            long len = snapshot.getChildrenCount();

                            for (DataSnapshot dsp : snapshot.getChildren()) {
                                String em = dsp.child("email_id").getValue().toString();
                                String userFriendId = dsp.getKey();
                                String name1 = dsp.child("name").getValue().toString();

                                if(em.equals(email))
                                {
                                    HashMap<String, String> hp = new HashMap<>();

                                    DatabaseReference reference = mDatabase.child("Pending").child(userFriendId).child(uid);
                                    hp.put("Name",name);

                                    reference.setValue(hp);

                                    Toast.makeText(getApplicationContext(),"Friend Request sent to "+name1,Toast.LENGTH_SHORT).show();
                                    break;
                                }
                                else {
                                    f++;
                                    if(len == f)
                                    {
                                        Toast.makeText(getApplicationContext(),"user not found",Toast.LENGTH_SHORT).show();
                                    }
                                }

                            }
                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Error Retrieving Data",Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new PendingAdapter(jNames, this));
    }


    @Override
    protected void onResume() {
        super.onResume();

        final String uid = FirebaseAuth.getInstance().getUid();
        DatabaseReference reference = mDatabase.child("Users").child(uid);

        reference.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String nm = snapshot.child("name").getValue().toString();

                name = nm;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getBaseContext(),"error in loading data",Toast.LENGTH_SHORT).show();
            }
        });

        jNames.clear();
        initRecyclerView();

        DatabaseReference reference1 = mDatabase.child("Pending").child(uid);
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String FriendUID = dsp.getKey();
                    String FriendName = dsp.child("Name").getValue().toString();

                    String temp = FriendName + "____" + uid + "____" + FriendUID;

                    jNames.add(temp);
                    recyclerView.getAdapter().notifyItemInserted(jNames.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Loading Pending Requests", Toast.LENGTH_SHORT).show();
            }
        });

    }
}
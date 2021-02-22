package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Objects;

public class Brigade extends AppCompatActivity {

    private ImageView bback;
    RecyclerView recyclerView;
    String team, nm;
    private DatabaseReference mDatabase;
    TextView teamna;
    Button post;
    Button refresh;
    EditText comment;
    LinearLayoutManager layoutManager;
    List<String> jNames = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_brigade);


        recyclerView = findViewById(R.id.recyclerView);
        post = findViewById(R.id.post_comment_btn);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        comment = findViewById(R.id.add_comment);
        refresh = findViewById(R.id.refresh_btn);

        teamna = findViewById(R.id.brigade_team_name);

        team = getIntent().getStringExtra("TEAM");
        nm = getIntent().getStringExtra("NAME");

        teamna.setText(team);

        loadComment(team);

        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadComment(team);
            }
        });

        final DatabaseReference reference = mDatabase.child("Comments").child(team);

        post.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String comm = comment.getText().toString();
                // do validation here
                int j = 0;

                if(comm.length()<1) {
                    j = 1;
                }

                if(j==0)
                {
                    HashMap<String, String> hp = new HashMap<>();

                    hp.put("Comment",comm);
                    hp.put("Name", nm);
                    String key = reference.push().getKey();

                    reference.child(key).setValue(hp);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Write Comment",Toast.LENGTH_SHORT).show();
                }

            }
        });


        bback = (ImageView) findViewById(R.id.bback);

        bback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent bback_intent = new Intent(Brigade.this, Home.class);
                startActivity(bback_intent);
            }
        });
    }

    private void initRecyclerView() {
        layoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new CommentAdapter(jNames, this));
    }

    public void loadComment(String Team)
    {
        DatabaseReference ref = mDatabase.child("Comments").child(Team);
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                jNames.clear();
                initRecyclerView();
                for (DataSnapshot dsp : snapshot.getChildren()) {
                    String name  = dsp.child("Name").getValue().toString();
                    String comment   = dsp.child("Comment").getValue().toString();
                    String full = name+"____"+comment;
                    jNames.add(full);
                    recyclerView.getAdapter().notifyItemInserted(jNames.size());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error retrieving comments", Toast.LENGTH_SHORT).show();
            }
        });

    }


}
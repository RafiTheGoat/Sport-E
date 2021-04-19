package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;

public class QuizSend extends AppCompatActivity {

    EditText correct,wrong1,wrong2,question;
    Button sendQuestion;
    private FirebaseUser fUser;
    String userId;
    int n;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_send);

        correct = findViewById(R.id.editTextTextPersonName3);
        wrong1 = findViewById(R.id.editTextTextPersonName2);
        wrong2 = findViewById(R.id.editTextTextPersonName);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        question = findViewById(R.id.editTextTextMultiLine);
        sendQuestion = findViewById(R.id.createaccount_btn2);

        sendQuestion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String q = question.getText().toString();
                final String c = correct.getText().toString();
                final String w1 = wrong1.getText().toString();
                final String w2 = wrong2.getText().toString();

                if(q.trim().length()==0)
                {
                    question.setError("Please Enter Valid Question");
                }
                else if(c.trim().length()==0)
                {
                    correct.setError("Please Enter Valid Answer");
                }
                else if(w1.trim().length()==0)
                {
                    wrong1.setError("Please Enter Valid Answer");
                }
                else if(w2.trim().length()==0)
                {
                    wrong2.setError("Please Enter Valid Answer");
                }
                else
                {
                    n = 0;
                    DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Friends");
                    reference1.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            for (DataSnapshot dsp : snapshot.getChildren()) {

                                String FriendUId = dsp.getKey();

                                HashMap<String, String> hp = new HashMap<>();
                                hp.put("UID", userId);
                                hp.put("Question",q);
                                hp.put("Correct",c);
                                hp.put("Wrong1",w1);
                                hp.put("Wrong2",w2);

                                DatabaseReference reference = FirebaseDatabase.getInstance().getReference().child("Users").child(FriendUId).child("Questions");

                                reference.child(reference.push().getKey()).setValue(hp);
                                n++;
                                if(n==1)
                                {
                                    Toast.makeText(getApplicationContext(),"Question Sent", Toast.LENGTH_SHORT).show();
                                }
                            }


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(),"Error Loading data", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });

    }
}
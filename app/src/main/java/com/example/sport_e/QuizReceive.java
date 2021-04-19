package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Random;

public class QuizReceive extends AppCompatActivity {

    TextView name1, question;
    RadioButton option1,option2,option3;
    private FirebaseUser fUser;
    String userId;
    Button submit;
    String c;
    int point;
    RadioGroup radioGroup;
    String key;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quiz_receive);


        name1 = findViewById(R.id.venue5);
        question = findViewById(R.id.textView34);
        option1 = findViewById(R.id.radioButton);
        option2 = findViewById(R.id.radioButton2);
        option3 = findViewById(R.id.radioButton3);
        submit = findViewById(R.id.createaccount_btn3);

        radioGroup = findViewById(R.id.radioGroup);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        userId = fUser.getUid();

        option1.setChecked(true);

        option1.setText("");
        option2.setText("");
        option3.setText("");


        DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(userId);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String p = snapshot.child("points").getValue().toString();
                point = Integer.parseInt(p);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(), "error loading data", Toast.LENGTH_SHORT).show();
            }
        });



        DatabaseReference reference1 = FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Questions");
        reference1.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {



                if(snapshot.getChildrenCount()==0)
                {
                    Toast.makeText(getApplicationContext(),"No More Questions Pending",Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(getApplicationContext(),Home.class);
                    startActivity(intent);
                    finish();
                }

                for (DataSnapshot dsp : snapshot.getChildren()) {



                    key = dsp.getKey();



                    String uid = dsp.child("UID").getValue().toString();
                    final String Correct = dsp.child("Correct").getValue().toString();
                    c = Correct;
                    final String Question = dsp.child("Question").getValue().toString();
                    final String wrong1 = dsp.child("Wrong1").getValue().toString();
                    final String wrong2 = dsp.child("Wrong2").getValue().toString();


                    DatabaseReference ref = FirebaseDatabase.getInstance().getReference().child("Users").child(uid);
                    ref.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {

                            String name = snapshot.child("name").getValue().toString();
                            name1.setText(name);

                            question.setText(Question);
                            Random random = new Random();
                            int i = random.nextInt(3);

                            if(i == 0)
                            {
                                option1.setText(Correct);
                                option2.setText(wrong1);
                                option3.setText(wrong2);
                            }
                            else if(i==1)
                            {
                                option1.setText(wrong1);
                                option2.setText(Correct);
                                option3.setText(wrong2);
                            }
                            else
                            {
                                option1.setText(wrong1);
                                option2.setText(wrong2);
                                option3.setText(Correct);
                            }

                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(getApplicationContext(), "error loading data", Toast.LENGTH_SHORT).show();
                        }
                    });


                    break;


                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Loading data", Toast.LENGTH_SHORT).show();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                int selectedId = radioGroup.getCheckedRadioButtonId();

                RadioButton radioButton = findViewById(selectedId);

                if(c.compareTo(radioButton.getText().toString())==0)
                {
                    Toast.makeText(getApplicationContext(),"You have selected the correct answer, score updated", Toast.LENGTH_SHORT).show();
                    point++;
                    FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("points").setValue(point);
                }
                else
                {
                    Toast.makeText(getApplicationContext(),"Correct Answer is "+c, Toast.LENGTH_SHORT).show();
                }
                FirebaseDatabase.getInstance().getReference().child("Users").child(userId).child("Questions").child(key).removeValue();
                Intent i = new Intent(getApplicationContext(), QuizReceive.class);
                startActivity(i);
                finish();
            }
        });

    }
}
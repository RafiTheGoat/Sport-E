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

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Fans extends AppCompatActivity {

    private ImageView fback;
    private RequestQueue mQueue;
    private String url;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID,teamna;
    private Button show;
    private TextView first,second, third;
    TextView firstPoint, secondPoint, thirdPoint;
    private DatabaseReference mDatabase;
    TextView yourscore;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fans);


        yourscore = findViewById(R.id.yourscore);
        first = findViewById(R.id.firstplace_username);
        second = findViewById(R.id.secondplace_username);
        third = findViewById(R.id.thirdplace_username);

        firstPoint = findViewById(R.id.firstplace_user_points);
        secondPoint = findViewById(R.id.secondplace_user_points);
        thirdPoint = findViewById(R.id.thirdplace_user_points);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        show = findViewById(R.id.show);
        mQueue = Volley.newRequestQueue(this);
        fback = (ImageView) findViewById(R.id.fback);
        fback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent fback_intent = new Intent(Fans.this, Home.class);
                startActivity(fback_intent);
            }
        });
        mQueue = Volley.newRequestQueue(this);
        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();


        updateFirstTime();



        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    teamna = userProfile.team;


                }

                //works here Toast.makeText(Standings.this, ""+teamna, Toast.LENGTH_SHORT).show();

                if(teamna !=null || teamna.equals("Arsenal")){

                    url = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/last/1?timezone=Europe%2FLondon";
                }
                else if(teamna !=null || teamna.equals("Juventus")){
                    //url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/next/1?timezone=Europe%2FLondon";//496
                    url = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/last/1?timezone=Europe%2FLondon"; //496
                }
                else if (teamna !=null || teamna.equals("Real Madrid" )){
                    //url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/next/1?timezone=Europe%2FLondon"; // 541
                    url = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/last/1?timezone=Europe%2FLondon";
                }

                    show.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            //jsonParse1();

                            String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                            final DatabaseReference ref = mDatabase.child("Predictions").child(uid);

                            ref.addValueEventListener(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    for (DataSnapshot dsp : snapshot.getChildren()) {
                                        String Prediction = dsp.child("Prediction").getValue().toString();
                                        String fix = dsp.getKey();
                                        jsonParse1(fix,Prediction);
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(getApplicationContext(),"Error Retrieving Data",Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    });





                //  also here Toast.makeText(Standings.this, ""+url, Toast.LENGTH_SHORT).show();



            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Fans.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });
    }





    private  void jsonParse1(final String fixture, final String Prediction){
        String URL ="https://api-football-v1.p.rapidapi.com/v2/fixtures/id/"+fixture+"?";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("fixtures");

                            for(int i = 0;i< jsonArray.length();i++)
                            {

                                JSONObject fixtures = jsonArray.getJSONObject(i);
                                String lastFID = fixtures.getString("fixture_id");
                                JSONObject hometeam = fixtures.getJSONObject("homeTeam");
                                JSONObject awayteam = fixtures.getJSONObject("awayTeam");
                                int goalH = fixtures.getInt("goalsHomeTeam");
                                int goalA = fixtures.getInt("goalsAwayTeam");
                                String hTname = hometeam.getString("team_name");
                                String aTname = awayteam.getString("team_name");
                                String status = fixtures.getString("status");

                                if(status.equals("Match Finished"))
                                {
                                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    final DatabaseReference ref = mDatabase.child("Predictions").child(uid);
                                    ref.child(fixture).removeValue();

                                    if(Prediction.equals("home"))
                                    {
                                        if(goalH>goalA)
                                        {
                                            add();
                                        }
                                    }
                                    else if (Prediction.equals("away"))
                                    {
                                        if(goalA>goalH)
                                        {
                                            add();
                                        }
                                    } else if (Prediction.equals("draw"))
                                    {
                                        if(goalA==goalH)
                                        {
                                            add();
                                        }
                                    }
                                }






                            }
                            //   }



                        } catch (JSONException e) {
                            e.printStackTrace();
                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();

            }
        }) {
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                Map<String, String> params = new HashMap<String,String>();
                params.put("x-rapidapi-host","api-football-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "d654579843msh1a3b8cd3ad5bc1dp194125jsnaed6fbcaf160");
                return params;
            }
        };
        mQueue.add(request);
    }

    public void add()
    {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = mDatabase.child("Users").child(uid);

        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                int point = Integer.parseInt(snapshot.child("points").getValue().toString());

                point = point + 1;

                mDatabase.child("Users").child(uid).child("points").setValue(point);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();

            }
        });

    }

    public void updateFirstTime()
    {
        final String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
        DatabaseReference ref = mDatabase.child("Users").child(uid);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String point = snapshot.child("points").getValue().toString();

                yourscore.setText(point);


            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error Occured",Toast.LENGTH_SHORT).show();
            }
        });




        DatabaseReference ref1 = mDatabase.child("Users");

        ref1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {

                String First = "", Second = "", Third = "";
                int firstScore=0, secondScore=0, thirdScore=0;



                for (DataSnapshot dsp : snapshot.getChildren()) {
                    int points = Integer.parseInt(dsp.child("points").getValue().toString());

                    String name = dsp.child("name").getValue().toString();



                    if(points>firstScore)
                    {
                        thirdScore = secondScore;
                        Third = Second;

                        secondScore = firstScore;
                        Second = First;

                        firstScore =points;
                        First = name;


                    }
                    else if(points>secondScore)
                    {
                            thirdScore = secondScore;
                            Third = Second;

                            secondScore = points;
                            Second = name;
                    }
                    else if(points>thirdScore)
                    {
                                thirdScore = points;
                                Third = name;
                    }


                    if(First.length()>0)
                    {
                        first.setText(First);
                        firstPoint.setText(""+firstScore);
                    }

                    if(Second.length()>0)
                    {
                        second.setText(Second);
                        secondPoint.setText(""+secondScore);
                    }

                    if(Third.length()>0)
                    {
                        third.setText(Third);
                        thirdPoint.setText(""+thirdScore);
                    }

                }



                }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(getApplicationContext(),"Error",Toast.LENGTH_SHORT).show();
            }
        });
    }

}
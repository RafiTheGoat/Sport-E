package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ActionBar;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
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

public class Home extends AppCompatActivity {

    private ImageView fans;
    private ImageView brigade;
    private ImageView predict;
    private TextView profile,ht1n,at1n,lghn,lgan,lghs,lgas,prev,next;
    private  TextView ven,venN,date,date_;
    private Button stand,show,hide,friends;
    private RequestQueue mQueue;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    String teamna;
    int points;
    String url,url1;

    String nameofuser;


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch(item.getItemId()) {

            case R.id.stand:
                //add the function to perform here
                Intent stnd = new Intent(Home.this, Standings.class);
                startActivity(stnd);

                return(true);
            case R.id.stat:
                //add the function to perform here
                Intent stats = new Intent(Home.this, stats.class);
                startActivity(stats);
                return(true);

            case R.id.prof:
                //add the function to perform here
                Intent prof = new Intent(Home.this, Profile.class);
                startActivity(prof);
                return(true);
        }
        return(super.onOptionsItemSelected(item));
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(getResources().getColor(android.R.color.transparent)));

         mQueue = Volley.newRequestQueue(this);
            ht1n = findViewById(R.id.team1_home1);
            show = findViewById(R.id.home_show);
            at1n = findViewById(R.id.team2_away1);


            lghn = findViewById(R.id.lghm);
            lgan = findViewById(R.id.lgma);
            lghs = findViewById(R.id.lgmhs);
            lgas = findViewById(R.id.lgmas);



            ven = findViewById(R.id.venue);
            venN = findViewById(R.id.ven_name);
            date = findViewById(R.id.date);
            date_ = findViewById(R.id.date_);
            hide = findViewById(R.id.home_hide);


            fans = (ImageView) findViewById(R.id.fans);
            brigade = (ImageView) findViewById(R.id.brigade);
            predict = (ImageView) findViewById(R.id.predict);
            friends = findViewById(R.id.friends);


            hide.setVisibility(View.GONE);
            ven.setVisibility(View.GONE);
            venN.setVisibility(View.GONE);
            date.setVisibility(View.GONE);
            date_.setVisibility(View.GONE);
            fans.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent fans_intent = new Intent(Home.this, Fans.class);
                    startActivity(fans_intent);
                }
            });





            friends.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent friends = new Intent(Home.this,SendRequest.class);
                    startActivity(friends);

                }
            });


            brigade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    fUser = FirebaseAuth.getInstance().getCurrentUser();
                    reference = FirebaseDatabase.getInstance().getReference("Users");
                    userID = fUser.getUid();

                    reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                            User userProfile = snapshot.getValue(User.class);

                            if(userProfile != null){
                                teamna = userProfile.team;
                                nameofuser = userProfile.name;
                                Intent brigade_intent = new Intent(Home.this, Brigade.class);
                                brigade_intent.putExtra("TEAM",teamna);
                                brigade_intent.putExtra("NAME",nameofuser);
                                startActivity(brigade_intent);

                            }
                            getSupportActionBar().setTitle(nameofuser);


                        }

                        @Override
                        public void onCancelled(@NonNull DatabaseError error) {
                            Toast.makeText(Home.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            });

            predict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent predict_intent = new Intent(Home.this, Predict.class);
                    startActivity(predict_intent);
                }
            });



        jsonParse();

        show.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ven.setVisibility(View.VISIBLE);
                venN.setVisibility(View.VISIBLE);
                date.setVisibility(View.VISIBLE);
                date_.setVisibility(View.VISIBLE);
               // show.setText("Hide Details");
                show.setVisibility(View.GONE);
                hide.setVisibility(View.VISIBLE);

            }

        });
        hide.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ven.setVisibility(View.GONE);
                venN.setVisibility(View.GONE);
                date.setVisibility(View.GONE);
                date_.setVisibility(View.GONE);
                hide.setVisibility(View.GONE);
                show.setVisibility(View.VISIBLE);
            }
        });

        mQueue = Volley.newRequestQueue(this);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();


        reference.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {

            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                User userProfile = snapshot.getValue(User.class);

                if(userProfile != null){
                    teamna = userProfile.team;
                    nameofuser = userProfile.name;

                }

                //works here Toast.makeText(Standings.this, ""+teamna, Toast.LENGTH_SHORT).show();

                if( teamna.equals("Arsenal")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/next/1?timezone=Europe%2FLondon";
                    url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/last/1?timezone=Europe%2FLondon";
                }
                else if( teamna.equals("Juventus")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/next/1?timezone=Europe%2FLondon";//496
                    url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/last/1?timezone=Europe%2FLondon"; //496
                }
               else if ( teamna.equals("Real Madrid" )){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/next/1?timezone=Europe%2FLondon"; // 541
                    url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/last/1?timezone=Europe%2FLondon";
                }




                //  also here Toast.makeText(Standings.this, ""+url, Toast.LENGTH_SHORT).show();

                jsonParse();
                jsonParse1();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Home.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });








    }
    private  void jsonParse(){
        String URL = url;
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("fixtures");
                            //Toast.makeText(Home.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

                            for(int i = 0;i< jsonArray.length();i++)
                            {
                              //  JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                               // for(int k=0;k<jsonArray1.length();k++) {
                                    JSONObject fixtures = jsonArray.getJSONObject(i);
                                    JSONObject hometeam = fixtures.getJSONObject("homeTeam");
                                    JSONObject awayteam = fixtures.getJSONObject("awayTeam");
                                    String goalH = fixtures.getString("goalsHomeTeam");
                                    String goalA = fixtures.getString("goalsAwayTeam");
                                    String hTname = hometeam.getString("team_name");
                                    String aTname = awayteam.getString("team_name");
                                    String mdate =  fixtures.getString("event_date");
                                    String venue_name = fixtures.getString("venue");




                                  //  t1n.append(hTname + "," + String.valueOf(id) + "," + String.valueOf(points) + "\n\n");
                                ht1n.append(hTname);
                                at1n.append(aTname);
                                venN.append(venue_name);
                                date_.append(mdate);

                                Intent intent = new Intent(getBaseContext(), Predict.class);
                                intent.putExtra("HTNAME", hTname);
                                intent.putExtra("ATNAME",aTname);
                               // startActivity(intent);
                               // SharedPreferences sharedPreferences = getSharedPreferences(SHARED)


                                }




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

    private  void jsonParse1(){
        String URL =url1;//"https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/last/1?timezone=Europe%2FLondon";
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
                                JSONObject hometeam = fixtures.getJSONObject("homeTeam");
                                JSONObject awayteam = fixtures.getJSONObject("awayTeam");
                                int goalH = fixtures.getInt("goalsHomeTeam");
                                int goalA = fixtures.getInt("goalsAwayTeam");
                                String hTname = hometeam.getString("team_name");
                                String aTname = awayteam.getString("team_name");


                                //  t1n.append(hTname + "," + String.valueOf(id) + "," + String.valueOf(points) + "\n\n");
                                lghn.append(hTname);
                                lgan.append(aTname);

                                lghs.append(String.valueOf(goalH));
                                lgas.append(String.valueOf(goalA));




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
}
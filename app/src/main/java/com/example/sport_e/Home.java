package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Home extends AppCompatActivity {

    private ImageView fans;
    private ImageView brigade;
    private ImageView predict;
    private TextView profile,ht1n,at1n,ht1s,at1s;
    private  TextView ven,venN,date,date_;
    private Button stand,show,hide;
    private RequestQueue mQueue;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);



         mQueue = Volley.newRequestQueue(this);
            ht1n = findViewById(R.id.team1_home1);
            show = findViewById(R.id.home_show);
            at1n = findViewById(R.id.team2_away1);

            ht1s = findViewById(R.id.team1_score1);

            at1s = findViewById(R.id.team2_score1);
            ven = findViewById(R.id.venue);
            venN = findViewById(R.id.ven_name);
            date = findViewById(R.id.date);
            date_ = findViewById(R.id.date_);
            hide = findViewById(R.id.home_hide);


            fans = (ImageView) findViewById(R.id.fans);
            brigade = (ImageView) findViewById(R.id.brigade);
            predict = (ImageView) findViewById(R.id.predict);
            profile = (TextView) findViewById(R.id.home_username);
            stand = (Button)findViewById(R.id.Home_show);


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

            brigade.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent brigade_intent = new Intent(Home.this, Brigade.class);
                    startActivity(brigade_intent);
                }
            });

            predict.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent predict_intent = new Intent(Home.this, Predict.class);
                    startActivity(predict_intent);
                }
            });

            profile.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent profile_intent = new Intent(Home.this, Profile.class);
                    startActivity(profile_intent);
                }
            });
            stand.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent Stand = new Intent(Home.this, Standings.class);
                    startActivity(Stand);
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

    }
    private  void jsonParse(){
        String URL = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/next/1?timezone=Europe%2FLondon";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("fixtures");
                           // Toast.makeText(Standings.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

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


                                  //  int points = fixtures.getInt("points");

                                  //  t1n.append(hTname + "," + String.valueOf(id) + "," + String.valueOf(points) + "\n\n");
                                ht1n.append(hTname);

                                ht1s.append(goalH);
                               // ht2n.append(hTname);
                                //ht2s.append(goalH);
                                at1n.append(aTname);
                                //at2n.append(aTname);
                                at1s.append(goalA);
                                venN.append(venue_name);
                                date_.append(mdate);
                                //at2s.append(goalA);
                                   // t2n.append(aTname+"\n\n\n\n");
                                   // t2s.append(goalA+"\n\n\n\n");


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
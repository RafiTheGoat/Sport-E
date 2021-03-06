package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.graphics.Color;
import android.os.Bundle;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.TableLayout;
import android.widget.TableRow;
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
import com.google.gson.JsonArray;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

public class Standings extends AppCompatActivity {
    private TextView mtext,mtext2,mtext3;
    private RequestQueue mQueue;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    String teamna;
    int points;
    String url;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);



        getSupportActionBar().hide();
        mtext = findViewById(R.id.view);
       // mtext2 = findViewById(R.id.view2);
       // mtext3 = findViewById(R.id.view3);
        //  Button show = findViewById(R.id.show);
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

                }
             //works here Toast.makeText(Standings.this, ""+teamna, Toast.LENGTH_SHORT).show();

                if(teamna.equals("Arsenal") || teamna.equals("Manchester United") || teamna.equals("Chelsea") || teamna.equals("Liverpool") || teamna.equals(("Manchester City"))){
                    url ="https://api-football-v1.p.rapidapi.com/v2/leagueTable/2790";
                }
                else if(teamna.equals("Juventus")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/leagueTable/2857";
                }
                else if (teamna.equals("Real Madrid") || teamna.equals("FC Barcelona")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/leagueTable/2833";
                }
             //  also here Toast.makeText(Standings.this, ""+url, Toast.LENGTH_SHORT).show();
                jsonParse();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Standings.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });








    }

    private  void jsonParse(){
         String URL =url;       //= "https://api-football-v1.p.rapidapi.com/v2/leagueTable/524";

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("standings");
                       //     Toast.makeText(Standings.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

                            for(int i = 0;i< jsonArray.length();i++)
                            {
                                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                                for(int k=0;k<jsonArray1.length();k++) {
                                    JSONObject standings = jsonArray1.getJSONObject(k);
                                    JSONObject all = standings.getJSONObject("all");
                                    int id = standings.getInt("rank");
                                    String Tname = standings.getString("teamName");
                                    points = standings.getInt("points");
                                    int mplayed = all.getInt("matchsPlayed");
                                    int won = all.getInt("win");
                                    int lost = all.getInt("lose");

                                            mtext.append(""+k + 1 + ". " + Tname + "\n" + "Points: " + String.valueOf(points) + "   " + "GP: " + String.valueOf(mplayed) + "   " + "Won: " + String.valueOf(won) + "   " + "Lost: " + String.valueOf(lost) + "\n------------------------------------------------"+"\n\n\n");




                                }
                                }


//                            This actually makes no difference because this itself is an async task. Youll have to continue to code from here onwards.
  //                          But i Hope you got my point. Suppose you want to use points in another function you call from here suppose


                                  // abc();
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
            public Map<String, String> getHeaders() throws AuthFailureError{
                Map<String, String> params = new HashMap<String,String>();
                params.put("x-rapidapi-host","api-football-v1.p.rapidapi.com");
                params.put("x-rapidapi-key", "d654579843msh1a3b8cd3ad5bc1dp194125jsnaed6fbcaf160");
                return params;
            }
        };
        mQueue.add(request);
    }



}
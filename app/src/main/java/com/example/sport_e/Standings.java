package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.VoiceInteractor;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
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
    private TextView mtext;
    private RequestQueue mQueue;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    private String team;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_standings);
        mtext = findViewById(R.id.view);
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
                     team = userProfile.team;



                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Standings.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

            }
        });
        Toast.makeText(this, ""+team, Toast.LENGTH_SHORT).show();
        jsonParse();






    }
    private  void jsonParse(){
        String URL = "https://api-football-v1.p.rapidapi.com/v2/leagueTable/524";
        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {

                            JSONObject api = response.getJSONObject("api");
                            JSONArray jsonArray = api.getJSONArray("standings");
                            Toast.makeText(Standings.this, ""+jsonArray.length(), Toast.LENGTH_SHORT).show();

                            for(int i = 0;i< jsonArray.length();i++)
                            {
                                JSONArray jsonArray1 = jsonArray.getJSONArray(i);
                                for(int k=0;k<jsonArray1.length();k++) {
                                    JSONObject standings = jsonArray1.getJSONObject(k);
                                    int id = standings.getInt("rank");
                                    String Tname = standings.getString("teamName");
                                    int points = standings.getInt("points");
                                    mtext.append(Tname + "," + String.valueOf(id) + "," + String.valueOf(points) + "\n\n");


                                }
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
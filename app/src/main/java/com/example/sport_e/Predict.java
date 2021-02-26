package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.provider.ContactsContract;
import android.view.View;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;
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

public class Predict extends AppCompatActivity {

    private ImageView pback;
    private Button hi,hd,ai,ad,place;
    private TextView home,away,hname,aname;
    private LottieAnimationView firework;
    int hscore = 0;
    int awscore = 0;
    private RequestQueue mQueue;
    private FirebaseUser fUser;
    private DatabaseReference reference;
    private String userID;
    String teamna,url,nameofuser;
    private RadioButton hrd,drd,ard;
    String fix;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getSupportActionBar().hide();
     //   mDatabase = FirebaseDatabase.getInstance().getReference();
        setContentView(R.layout.activity_predict);
       // hi = findViewById(R.id.hsi); // removed since not taking score as of now
       // hd = findViewById(R.id.hsd);
       // ai = findViewById(R.id.asi);
        //ad = findViewById(R.id.asd);
        hname = findViewById(R.id.predict_hometeam_name);
        aname = findViewById(R.id.predict_awayteam_name);
       // home = findViewById(R.id.predict_hometeam_score);
        //away = findViewById(R.id.predict_awayteam_score);
        place = findViewById(R.id.placeprediction_btn);
        firework = findViewById(R.id.predict_animation);

        hrd = findViewById(R.id.hrdbtn);
        drd = findViewById(R.id.drdbtn);
        ard = findViewById(R.id.ardbtn);

/*
        hi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                hscore++;
                home.setText(String.valueOf(hscore));


            }
        });
        hd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (hscore > 0) {
                    hscore--;
                    home.setText(String.valueOf(hscore));
                }
            }
        });
        ai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                    awscore++;
                    away.setText(String.valueOf(awscore));
                }

        });
        ad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (awscore > 0) {
                    awscore--;
                    away.setText(String.valueOf(awscore));
                }
            }
        });
        */

        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                if(hrd.isChecked())
                {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Predictions").child(uid).child(fix);

                    HashMap<String, String> hp = new HashMap<>();
                    hp.put("Prediction","home");

                    reference.setValue(hp);
                    Toast.makeText(getApplicationContext(),"Added Prediction for Home team to win",Toast.LENGTH_SHORT).show();

                }
                else if(ard.isChecked())
                {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Predictions").child(uid).child(fix);

                    HashMap<String, String> hp = new HashMap<>();
                    hp.put("Prediction","away");

                    reference.setValue(hp);
                    Toast.makeText(getApplicationContext(),"Added Prediction for Away team to win",Toast.LENGTH_SHORT).show();

                }
                else if(drd.isChecked())
                {
                    String uid = FirebaseAuth.getInstance().getCurrentUser().getUid();
                    reference = FirebaseDatabase.getInstance().getReference("Predictions").child(uid).child(fix);

                    HashMap<String, String> hp = new HashMap<>();
                    hp.put("Prediction","draw");

                    reference.setValue(hp);
                    Toast.makeText(getApplicationContext(),"Added Prediction for a draw",Toast.LENGTH_SHORT).show();

                }




                firework.playAnimation();

                firework.addAnimatorListener(new Animator.AnimatorListener() {
                    @Override
                    public void onAnimationStart(Animator animation) {

                    }

                    @Override
                    public void onAnimationEnd(Animator animation) {
                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Animation fadeOut = new AlphaAnimation(1, 0);
                                fadeOut.setInterpolator(new AccelerateInterpolator()); //and this
                                fadeOut.setStartOffset(1000);
                                fadeOut.setDuration(500);
                                AnimationSet animationSet = new AnimationSet(false);
                                animationSet.addAnimation(fadeOut);
                                firework.setAnimation(animationSet);
                                firework.setVisibility(View.GONE);
                            }
                        },2000);
                    }

                    @Override
                    public void onAnimationCancel(Animator animation) {

                    }

                    @Override
                    public void onAnimationRepeat(Animator animation) {

                    }
                });
            }
        });







        pback = (ImageView) findViewById(R.id.pback);

        pback.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent pback_intent = new Intent(Predict.this, Home.class);
                startActivity(pback_intent);
            }
        });




        String Htname = getIntent().getStringExtra("HTNAME");
        String Atname = getIntent().getStringExtra("ATNAME");

        hname.setText(Htname);
        aname.setText(Atname);



        //new after this
        mQueue = Volley.newRequestQueue(this);

        fUser = FirebaseAuth.getInstance().getCurrentUser();
        reference = FirebaseDatabase.getInstance().getReference("Users");
        userID = fUser.getUid();




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

                if(teamna.equals("Arsenal")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/next/1?timezone=Europe%2FLondon";
                    //url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/42/last/1?timezone=Europe%2FLondon";
                }
                else if(teamna.equals("Juventus")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/next/1?timezone=Europe%2FLondon";//496
                   // url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/496/last/1?timezone=Europe%2FLondon"; //496
                }
                else if (teamna.equals("Real Madrid")){
                    url ="https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/next/1?timezone=Europe%2FLondon"; // 541
                   // url1 = "https://api-football-v1.p.rapidapi.com/v2/fixtures/team/541/last/1?timezone=Europe%2FLondon";
                }


                //  also here Toast.makeText(Standings.this, ""+url, Toast.LENGTH_SHORT).show();

                jsonParse();
                //jsonParse1();

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(Predict.this, "Something went Wrong!!!", Toast.LENGTH_SHORT).show();

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
                                String fixID = fixtures.getString("fixture_id");

                                fix = fixID;


                                //  t1n.append(hTname + "," + String.valueOf(id) + "," + String.valueOf(points) + "\n\n");
                                hname.append(hTname);
                                aname.append(aTname);

                              //  venN.append(venue_name);
                               // date_.append(mdate);




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




}
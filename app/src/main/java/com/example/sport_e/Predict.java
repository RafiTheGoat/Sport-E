package com.example.sport_e;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.Animator;
import android.content.Intent;
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
import android.widget.TextView;
import android.widget.Toast;

import com.airbnb.lottie.LottieAnimationView;

public class Predict extends AppCompatActivity {

    private ImageView pback;
    private Button hi,hd,ai,ad,place;
    private TextView home,away;
    private LottieAnimationView firework;
    int hscore = 0;
    int awscore = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_predict);
        hi = findViewById(R.id.hsi);
        hd = findViewById(R.id.hsd);
        ai = findViewById(R.id.asi);
        ad = findViewById(R.id.asd);
        home = findViewById(R.id.predict_hometeam_score);
        away = findViewById(R.id.predict_awayteam_score);
        place = findViewById(R.id.placeprediction_btn);
        firework = findViewById(R.id.predict_animation);


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
        place.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(Predict.this, "Prediction Is placed!!", Toast.LENGTH_SHORT).show();
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
    }
}
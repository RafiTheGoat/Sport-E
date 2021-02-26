package com.example.sport_e;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    private TextView createaccount;
    private Button login_btn;
    private EditText user;
    private EditText pwd;
    private FirebaseAuth mAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getSupportActionBar().hide();

        createaccount = (TextView) findViewById(R.id.create_acc);
        login_btn = (Button) findViewById(R.id.login_btn);
        user = (EditText) findViewById(R.id.edit_email);
        pwd = (EditText) findViewById(R.id.edit_password);
        mAuth =FirebaseAuth.getInstance();

        createaccount.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent create = new Intent(MainActivity.this, Choose_Brigade.class);
                startActivity(create);
            }
        });


        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                userLogin();

                /*
                String un = user.getText().toString();
                String p = pwd.getText().toString();
                if(p.matches("") || un.matches("")){
                    Toast.makeText(MainActivity.this, "Required Field(s) Empty !",Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent login = new Intent(MainActivity.this, Home.class);
                    startActivity(login);
                }
                */
            }

            

        });
    }

    private void userLogin() {
        String email = user.getText().toString().trim();
        String password = pwd.getText().toString().trim();

        if(email.isEmpty()){
            user.setError("Email is required");
            user.requestFocus();
            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            user.setError(" Please enter valid Email ID");
            user.requestFocus();
            return;

        }
        if(password.isEmpty()){
            pwd.setError("Password is required!");
            pwd.requestFocus();
            return;
        }
        if(password.length() < 6){
            pwd.setError("Minimum 6 characters!");
            pwd.requestFocus();
            return;
        }

        mAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()) {
                    FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                    if (user.isEmailVerified()){

                        Intent home = new Intent(MainActivity.this, Home.class);
                    startActivity(home);
                }else{
                        user.sendEmailVerification();
                        Toast.makeText(MainActivity.this, "Check Your Email to Verify Your Account", Toast.LENGTH_LONG).show();
                    }
                }else{
                    Toast.makeText(MainActivity.this, "Wrong Email or Password", Toast.LENGTH_SHORT).show();

                }

            }
        });

    }
}


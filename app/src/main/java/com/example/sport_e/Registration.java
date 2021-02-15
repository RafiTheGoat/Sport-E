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
import com.google.firebase.database.FirebaseDatabase;
import com.example.sport_e.User;
import org.w3c.dom.Text;

import java.util.regex.Pattern;

public class Registration extends AppCompatActivity {
    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^" +
                    //"(?=.*[0-9])" +         //at least 1 digit
                    //"(?=.*[a-z])" +         //at least 1 lower case letter
                    //"(?=.*[A-Z])" +         //at least 1 upper case letter
                    "(?=.*[a-zA-Z])" +      //any letter
                    "(?=.*[@#$%^&+=])" +    //at least 1 special character
                    "(?=\\S+$)" +           //no white spaces
                    ".{4,}" +               //at least 4 characters
                    "$");

    private FirebaseAuth mAuth;

    private Button Create_account;
    private TextView login_redirect;
    private EditText fullname;
    private EditText username;
    private EditText email;
    private EditText phone;
    private EditText pass;
    private EditText confirmpass;
//    private User user = new User(null,null);
   private String team;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        mAuth = FirebaseAuth.getInstance();

        Create_account = (Button) findViewById(R.id.createaccount_btn);
        login_redirect = (TextView) findViewById(R.id.login_redirect);

        fullname = (EditText) findViewById(R.id.enter_name);
        username = (EditText) findViewById(R.id.enter_username);
        email = (EditText) findViewById(R.id.enter_email);
        phone = (EditText) findViewById(R.id.enter_phone);
        pass = (EditText) findViewById(R.id.enter_pass);
        confirmpass = (EditText) findViewById(R.id.enter_confirmpass);

        /*
        Create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String a = fullname.getText().toString();
                String b = username.getText().toString();
                String c = email.getText().toString();
                String d = phone.getText().toString();
                String e = pass.getText().toString();
                String f = confirmpass.getText().toString();
                if(a.matches("") || b.matches("") || c.matches("") || d.matches("") || e.matches("") || f.matches("")){
                    Toast.makeText(Registration.this, "Required Field(s) Empty !",Toast.LENGTH_SHORT).show();
                }
                else if(!e.equals(f)){
                    Toast.makeText(Registration.this, "Passwords Do Not Match!", Toast.LENGTH_SHORT).show();
                }
                else {
                    Intent creating = new Intent(Registration.this, Choose_Brigade.class);
                    startActivity(creating);
                }
            }

        });

         */

        login_redirect.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent redirect = new Intent(Registration.this, MainActivity.class);
                startActivity(redirect);
            }
        });
        Create_account.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                register();


            }
        });


    }

    public void register(){
         final String name= fullname.getText().toString().trim();
        String usern= username.getText().toString().trim();
         final String email_id= email.getText().toString().trim();
        String phno = phone.getText().toString().trim();
        String password = pass.getText().toString().trim();
        String conpass= confirmpass.getText().toString().trim();
        Intent intent=getIntent();
        final String team = intent.getStringExtra("team");


        if(name.isEmpty()){
            fullname.setError("Fullname is required");
            fullname.requestFocus();

            return;
        }
        if(usern.isEmpty()){
            username.setError("username is required");
            username.requestFocus();

            return;
        }
        if(email_id.isEmpty()){
            email.setError("email is required");
            email.requestFocus();

            return;
        }
        if(!Patterns.EMAIL_ADDRESS.matcher(email_id).matches()){
            email.setError("Enter valid Email ID");
            email.requestFocus();

            return;
        }
        if(phno.isEmpty()){
            phone.setError("Phone number is required");
            phone.requestFocus();

            return;
        }
        if(password.isEmpty()){
            pass.setError("password is required");
            pass.requestFocus();

            return;
        }
        if(password.length() < 6){
            pass.setError("password can not be less than 6 characters");
            pass.requestFocus();

            return;
        }


        /* if (!PASSWORD_PATTERN.matcher(password).matches()) {
            pass.setError("Password too weak");
            return;
        }

         */
        if(conpass.isEmpty()){
            confirmpass.setError("This field is required");
            confirmpass.requestFocus();

            return;
        }


        if(!conpass.equals(password)){
            confirmpass.setError("Passwords do not match!");
            confirmpass.requestFocus();

            return;
        }

       /* Intent creating = new Intent(Registration.this, Choose_Brigade.class);
        creating.putExtra("name",name);
        creating.putExtra("email",email_id);
        creating.putExtra("pass",password);
        startActivity(creating);

        */

       mAuth.createUserWithEmailAndPassword(email_id,password)
                .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            User user = new User(name,email_id,team);
                            FirebaseDatabase.getInstance().getReference("Users")
                                    .child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                                    .setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(Registration.this, "User has been registered!",Toast.LENGTH_LONG).show();
                                        Intent creating = new Intent(Registration.this, MainActivity.class);
                                        startActivity(creating);

                                    }
                                }
                            });
                        }
                    }
                });




    }
}
package com.sunain.sampleapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.sunain.sampleapp.R;

import es.dmoral.toasty.Toasty;

public class SignupActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    String TAG="SignupActivity";
    Button bsignup,blogin;
    TextView tv_name,tv_email,tv_pass,tv_cpass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        mAuth = FirebaseAuth.getInstance();
        blogin=findViewById(R.id.signup_login);
        bsignup=findViewById(R.id.signup_button);
        tv_name=findViewById(R.id.signup_name);
        tv_email=findViewById(R.id.signup_email);
        tv_pass=findViewById(R.id.signup_password);
        tv_cpass=findViewById(R.id.signup_cpassword);
        login_press();
        signup_press();
    }
    private void signup_press()
    {
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                check_validation();

            }
        });
    }
    private void check_validation()
    {
        String email=tv_email.getText().toString().trim();
        String pass=tv_pass.getText().toString().trim();
        String cpass=tv_cpass.getText().toString().trim();
        String name=tv_name.getText().toString().trim();
        if(email.length()==0||pass.length()==0||cpass.length()==0)
        {
            Toasty.error(getApplicationContext(), "Enter All Fields", Toast.LENGTH_SHORT, true).show();
            return;
        }
        if(!pass.equals(cpass))
        {
            Toasty.error(getApplicationContext(), "password does not match", Toast.LENGTH_SHORT, true).show();
            return;
        }
        signup_func(email,pass,name);
    }
    private void login_press()
    {
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    private void signup_func(String email, String password, final String name)
    {
        mAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
                            Log.d(TAG, "createUserWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            if(user!=null)
                            {
                                UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(name).build();
                                user.updateProfile(profileUpdates);
                                FirebaseAuth.getInstance().signOut();
                                Toasty.success(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT, true).show();
//                                Toasty.success(getApplicationContext(), "Check email for verification link", Toast.LENGTH_SHORT, true).show();
                                Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                                startActivity(i);
                                finish();
//                                sendVerificationEmail();
                            }
                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "createUserWithEmail:failure", task.getException());
//                            Toast.makeText(getApplicationContext(), "Authentication failed.",Toast.LENGTH_SHORT).show();
                            Toasty.error(getApplicationContext(), "Error in Sign Up", Toast.LENGTH_SHORT, true).show();
                        }
                    }
                });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i=new Intent(SignupActivity.this,LoginActivity.class);
        startActivity(i);
        finish();

    }

    private void sendVerificationEmail()
    {
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            FirebaseAuth.getInstance().signOut();
                            Toasty.success(getApplicationContext(), "Successfully registered!", Toast.LENGTH_SHORT, true).show();
                            Toasty.success(getApplicationContext(), "Check email for verification link", Toast.LENGTH_SHORT, true).show();
                            Intent i=new Intent(SignupActivity.this,LoginActivity.class);
                            startActivity(i);
                            finish();
                        }
                        else
                        {
                            // email not sent, so display message and restart the activity or do whatever you wish to do

                            //restart this activity
                            overridePendingTransition(0, 0);
                            finish();
                            overridePendingTransition(0, 0);
                            startActivity(getIntent());

                        }
                    }
                });
    }


    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()!=null)
        {
            Intent i=new Intent(this,MainActivity.class);
            startActivity(i);
            finish();
        }
    }
}

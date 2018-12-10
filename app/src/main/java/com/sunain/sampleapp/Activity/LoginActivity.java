package com.sunain.sampleapp.Activity;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.sunain.sampleapp.R;

import es.dmoral.toasty.Toasty;

public class LoginActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String TAG="LoginActivity";
    Button blogin,bforgetpass,bsignup;
    TextView tv_email,tv_pass;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mAuth = FirebaseAuth.getInstance();
        blogin=findViewById(R.id.login_button);
        bforgetpass=findViewById(R.id.login_forget_password);
        bsignup=findViewById(R.id.login_sign_up);
        tv_email=findViewById(R.id.login_email);
        tv_pass=findViewById(R.id.login_password);
        login_press();
        forget_press();
        signup_press();
    }

    private void forget_press()
    {

    }
    private void signup_press()
    {
        bsignup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i=new Intent(LoginActivity.this,SignupActivity.class);
                startActivity(i);
            }
        });
    }
    private void login_press()
    {
        blogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(tv_email.getText().toString().length()==0||tv_pass.getText().toString().length()==0)
                {
                  Toasty.error(getApplicationContext(),"Enter email and Password",Toast.LENGTH_LONG).show();
                  return;
                }
                signin_func(tv_email.getText().toString(),tv_pass.getText().toString());
            }
        });
    }
    private void signin_func(String email,String password)
    {

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        if (task.isSuccessful()) {
                            // Sign in success, update UI with the signed-in user's information
//                            apb.setProgress(100);
                            Log.d(TAG, "signInWithEmail:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            Intent i=new Intent(LoginActivity.this,MainActivity.class);
                            startActivity(i);
                            finish();

                        } else {
                            // If sign in fails, display a message to the user.
                            Log.w(TAG, "signInWithEmail:failure", task.getException());
                            Toasty.error(getApplicationContext(), "Incorrect email or password",
                                    Toast.LENGTH_SHORT).show();

                        }
                        // ...
                    }
                }).addOnFailureListener(this, new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toasty.error(getApplicationContext(), "Incorrect email or password",
                        Toast.LENGTH_SHORT).show();
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

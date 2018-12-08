package com.sunain.sampleapp.Activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.sunain.sampleapp.Activity.LoginActivity;
import com.sunain.sampleapp.R;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    String TAG="MainActivity";
    Button bt_signout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        bt_signout=findViewById(R.id.main_signout);
        signout_press();
    }

    private void signout_press()
    {
        bt_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent i= new Intent(MainActivity.this,LoginActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
    @Override
    protected void onStart() {
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser()==null)
        {
            Intent i= new Intent(this,LoginActivity.class);
            startActivity(i);
            finish();
        }
    }
}

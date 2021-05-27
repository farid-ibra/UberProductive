package com.example.uberproductive.controller;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.uberproductive.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SplashScreen extends AppCompatActivity {
    FirebaseAuth firebaseAuth;
    Animation splashAnim;
    ImageView logo;
    TextView appName;
    FirebaseUser currentUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_activity);
        firebaseAuth = FirebaseAuth.getInstance();
        splashAnim = AnimationUtils.loadAnimation(this, R.anim.splash);
        logo = findViewById(R.id.AppLogo);
        logo.setAnimation(splashAnim);
        appName = findViewById(R.id.AppName);
        appName.setAnimation(splashAnim);
        currentUser = firebaseAuth.getCurrentUser();

        Handler mHandler = new Handler();
        mHandler.postDelayed(new Runnable() {
            @Override
            public void run() {
                checkIfUserExists();
            }
                },4000);
    }
    public void checkIfUserExists(){
        if(currentUser != null){
            startActivity(new Intent(getApplicationContext(), Dashboard.class));
            finish();
        }
        else {
            createTemporaryUser();
        }
    }

    public void createTemporaryUser(){
        firebaseAuth.signInAnonymously().addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {
                Toast.makeText(SplashScreen.this, "Temporary account is created", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(getApplicationContext(), Dashboard.class));
                finish();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SplashScreen.this, "Couldn't create temporary account " + e.getMessage(), Toast.LENGTH_SHORT).show();
                finish();
            }
        });
    }
}

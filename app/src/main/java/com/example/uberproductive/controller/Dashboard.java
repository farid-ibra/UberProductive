package com.example.uberproductive.controller;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.uberproductive.GoalTracker;
import com.example.uberproductive.NoteTaker;
import com.example.uberproductive.R;
import com.example.uberproductive.PomodoroTimer;

import androidx.appcompat.app.AppCompatActivity;

public class Dashboard extends AppCompatActivity {
    ImageView note;
    ImageView pomodoro;
    ImageView goal;
    TextView textNote;
    TextView textGoal;
    TextView textPomodoro;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dashboard);
        note =  findViewById(R.id.note);
        pomodoro = findViewById(R.id.pomodoro);
        goal = findViewById(R.id.goal);
        textNote = findViewById(R.id.textView6);
        textGoal = findViewById(R.id.textView4);
        textPomodoro = findViewById(R.id.textView5);

        note.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NoteTaker.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        textNote.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), NoteTaker.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        pomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PomodoroTimer.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        textPomodoro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), PomodoroTimer.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        goal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GoalTracker.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });

        textGoal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getApplicationContext(), GoalTracker.class));
                overridePendingTransition(R.anim.slide_up, R.anim.slide_down);
            }
        });
    }
}

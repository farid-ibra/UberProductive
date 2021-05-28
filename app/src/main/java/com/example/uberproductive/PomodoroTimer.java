package com.example.uberproductive;
import android.annotation.SuppressLint;
import android.media.MediaPlayer;
import android.os.CountDownTimer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.RelativeLayout;
import android.widget.Switch;
import android.widget.TextView;

import java.util.Locale;
public class PomodoroTimer extends AppCompatActivity {
    public static long TIME_IN_MILLISECONDS = 1500000;
    private TextView wTextViewCountDown;
    @SuppressLint("UseSwitchCompatOrMaterialCode")
    private Switch wSwitch;
    private Button wButtonStartPause;
    private Button wButtonReset;
    private CountDownTimer wCountDownTimer;
    private boolean wTimerRunning;
    private long wTimeLeftInMillis = TIME_IN_MILLISECONDS;
    RelativeLayout currentLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.pomodoro_timer);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        wSwitch = findViewById(R.id.switch1);
        wTextViewCountDown = findViewById(R.id.text_view_countdown);
        wButtonStartPause = findViewById(R.id.button_start_pause);
        wButtonReset = findViewById(R.id.button_reset);
        currentLayout = (RelativeLayout) findViewById(R.id.pomodorolayout);

        wSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean on) {
                if(on){
                    wSwitch.setText("Focus");
                    pauseTime();
                    wTextViewCountDown.setText("05:00");
                    wTimeLeftInMillis = 300000;
                    TIME_IN_MILLISECONDS = 300000;
                    resetTime();
                    currentLayout.setBackgroundColor(getResources().getColor(R.color.pomodoroGreen));
                }
                else{
                    wSwitch.setText("Break");
                    pauseTime();
                    wTextViewCountDown.setText("25:00");
                    wTimeLeftInMillis = 1500000;
                    TIME_IN_MILLISECONDS = 1500000;
                    resetTime();
                    currentLayout.setBackgroundColor(getResources().getColor(R.color.pomodoroRed));
                }
            }
        });

        wButtonStartPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (wTimerRunning) {
                    pauseTime();
                } else {
                    startTime();
                }
            }
        });

        wButtonReset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                resetTime();
            }
        });
        updateTime();
    }

    private void startTime() {
        wSwitch.setVisibility(View.VISIBLE);
        wCountDownTimer = new CountDownTimer(wTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                wTimeLeftInMillis = millisUntilFinished;
                updateTime();
            }
            @Override
            public void onFinish() {
                final MediaPlayer sound = MediaPlayer.create(PomodoroTimer.this, R.raw.sound_effect);
                wTimerRunning = false;
                sound.start();
                wButtonStartPause.setText("Start");
                wButtonStartPause.setVisibility(View.INVISIBLE);
                wButtonReset.setVisibility(View.VISIBLE);
            }
        }.start();

        wTimerRunning = true;
        wButtonStartPause.setText("Pause");
        wButtonReset.setVisibility(View.INVISIBLE);
    }
    private void pauseTime() {
        wCountDownTimer.cancel();
        wTimerRunning = false;
        wButtonStartPause.setText("Start");
        wButtonReset.setVisibility(View.VISIBLE);
    }
    private void resetTime() {
        wTimeLeftInMillis = TIME_IN_MILLISECONDS;
        updateTime();
        wButtonReset.setVisibility(View.INVISIBLE);
        wButtonStartPause.setVisibility(View.VISIBLE);
    }
    private void updateTime() {
        String timeLeft = String.format(Locale.getDefault(),
                "%2d:%02d",
                (int) (wTimeLeftInMillis / 1000) / 60, (int) (wTimeLeftInMillis / 1000) % 60); //minutes and seconds
        wTextViewCountDown.setText(timeLeft);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
        TIME_IN_MILLISECONDS = 1500000;
        wTextViewCountDown.setText("25:00");
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.close_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if(item.getItemId() == R.id.close){
            onBackPressed();
        }
        return super.onOptionsItemSelected(item);
    }
}
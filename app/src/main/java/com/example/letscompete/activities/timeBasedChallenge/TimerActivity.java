package com.example.letscompete.activities.timeBasedChallenge;

import android.os.Bundle;
import android.os.Handler;
import android.os.SystemClock;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.letscompete.R;

public class TimerActivity<storageReference> extends AppCompatActivity {
    private static final String TAG = TimerActivity.class.getSimpleName();
    //Chronometer
    private Chronometer chronometer;
    private boolean running;
    private long pauseOffset;
    Button startbtn, pausebtn, resetbtn;
    private Handler timerHandler;
    private Runnable timerRunnable;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_time_complete_challenge);
        //TIMER
        timerHandler = new Handler();
        timerRunnable = new Runnable() {

            @Override
            public void run() {
                Log.d(TAG, "TICK");
                timerHandler.postDelayed(timerRunnable, 1000);
            }
        };
        chronometer = (Chronometer) findViewById(R.id.complete_chronometer);
        chronometer.setFormat("Time: %s");
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.setCountDown(true);
        startbtn = (Button) findViewById(R.id.startChronometer);
        pausebtn = (Button) findViewById(R.id.pauseChronometer);
        resetbtn = (Button) findViewById(R.id.resetChronometer);
        //button listener
        startbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!running) {
                    chronometer.setBase(SystemClock.elapsedRealtime() - pauseOffset);
                    chronometer.start();
                    running = true;
                }
            }
        });
        pausebtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (running) {
                    chronometer.stop();
                    pauseOffset = SystemClock.elapsedRealtime() - chronometer.getBase();
                    running = false;
                }
            }
        });
        resetbtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                chronometer.setBase(SystemClock.elapsedRealtime());
                pauseOffset = 0;
            }
        });

    }

    public void startTimer() {
        Log.d(TAG, "Timer started");
        timerHandler.post(timerRunnable);
    }

    public void stopTimer() {
        Log.d(TAG, "Timer stopped");
        timerHandler.removeCallbacks(timerRunnable);
    }
}

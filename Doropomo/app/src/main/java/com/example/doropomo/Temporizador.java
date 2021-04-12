package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Button;
import com.example.doropomo.utilidades.Inicio;

import java.util.Locale;
import java.util.Timer;
import java.util.TimerTask;

public class Temporizador extends AppCompatActivity {

    private static final long START_TIME_IN_MILLIS = 100000;

    private TextView mTextViewCountdown;

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonReset;
    private Button mButtonClean;
    private Button mButtonConfig;

    private CountDownTimer mCountDownTimer;
    private boolean mTimerRunning;
    private long mTimeLeftInMillis = START_TIME_IN_MILLIS;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizador);

        mTextViewCountdown = findViewById(R.id.txtTimer);
        mButtonStart = findViewById(R.id.button_start);
        mButtonPause = findViewById(R.id.button_pause);
        mButtonReset = findViewById(R.id.button_restart);
        mButtonClean = findViewById(R.id.button_clean);
        mButtonConfig = findViewById(R.id.button_config);

        mButtonPause.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                pauseTimer();
            }
        });

        mButtonPause.setVisibility(View.INVISIBLE);

        mButtonReset.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                resetTimer();
            }
        });

        mButtonClean.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                cleanTimer();
            }
        });

        mButtonStart.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                startTimer();
            }
        });

        mButtonConfig.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                openConfigView();
            }
        });
    }

    private void startTimer(){
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                updateCountDownText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                showCountDownStoppedButtons();
            }
        }.start();

        mTimerRunning = true;

        hideCountDownStoppedButtons();
    }

    private void pauseTimer(){
        mCountDownTimer.cancel();
        mTimerRunning = false;

        showCountDownStoppedButtons();
    }

    private void resetTimer(){
        mTimeLeftInMillis = START_TIME_IN_MILLIS;
        updateCountDownText();

        showCountDownStoppedButtons();
    }

    private void cleanTimer(){

    }

    private void openConfigView(){
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Temporizador.this, Configuracion.class);
                startActivity(intent);
                Temporizador.this.finish();
            }
        }, 0);
    }

    private void updateCountDownText(){
        int minutes = (int)mTimeLeftInMillis / 1000 / 60;
        int seconds = (int)mTimeLeftInMillis / 1000 % 60;

        String timeLeftFormatted = String.format(Locale.getDefault(),"%02d:%02d", minutes, seconds);
        mTextViewCountdown.setText(timeLeftFormatted);
    }

    private void showCountDownStoppedButtons(){
        mButtonPause.setVisibility(View.INVISIBLE);
        mButtonStart.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.VISIBLE);
        mButtonClean.setVisibility(View.VISIBLE);
    }

    private void hideCountDownStoppedButtons(){
        mButtonPause.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.INVISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonClean.setVisibility(View.INVISIBLE);
    }
}

package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Button;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;
import java.util.Timer;
import java.util.TimerTask;

public class Temporizador extends AppCompatActivity {

    private final short LIMIT_SHORT_PAUSES = 4;

    private TextView mTextViewCountdown;
    private TextView mTextViewSesionIniciada;
    private TextView mTextViewFinaliza;
    private  TextView mTextViewEstado;

    private Button mButtonStart;
    private Button mButtonPause;
    private Button mButtonReset;
    private Button mButtonClean;
    private Button mButtonConfig;

    private CountDownTimer mCountDownTimer;
    private ProgressBar mProgressBar;
    private boolean mTimerRunning;
    private boolean mIsCurrentlyInBreak = false;
    private long mTimeLeftInMillis;
    private long mEndTime;

    private long sesionTrabajoInMillis;
    private long pausaLargaInMillis;
    private long pausaCortaInMillis;

    private short mPausesCounter = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_temporizador);

        mTextViewCountdown = findViewById(R.id.txtTimer);
        mTextViewSesionIniciada = findViewById(R.id.txtTimeSesionIniciada);
        mTextViewFinaliza = findViewById(R.id.txtTimeSesionFinaliza);
        mTextViewEstado = findViewById(R.id.txtEstado);

        mButtonStart = findViewById(R.id.button_start);
        mButtonPause = findViewById(R.id.button_pause);
        mButtonReset = findViewById(R.id.button_restart);
        mButtonClean = findViewById(R.id.button_clean);
        mButtonConfig = findViewById(R.id.button_config);

        mProgressBar = findViewById(R.id.progressBar);

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

        mTextViewSesionIniciada.setText(" ");
        mTextViewFinaliza.setText(" ");
    }

    private void startTimer(){

        mEndTime = System.currentTimeMillis() + mTimeLeftInMillis;
        mTimerRunning = true;
        mCountDownTimer = new CountDownTimer(mTimeLeftInMillis, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                mTimeLeftInMillis = millisUntilFinished;
                mProgressBar.incrementProgressBy(1);

                updateCountDownText();
                updateDetailsText();
            }

            @Override
            public void onFinish() {
                mTimerRunning = false;
                mProgressBar.setProgress(0);
                mProgressBar.refreshDrawableState();

                if(mIsCurrentlyInBreak){
                    mTextViewEstado.setText("Sesión de Trabajo");
                    mTimeLeftInMillis = sesionTrabajoInMillis;
                    mProgressBar.setMax((int)sesionTrabajoInMillis / 1000);
                }
                else{

                    if(mPausesCounter <= LIMIT_SHORT_PAUSES){
                        mTextViewEstado.setText("Descanso corto " + mPausesCounter);
                        mTimeLeftInMillis = pausaCortaInMillis;
                        mProgressBar.setMax((int)pausaCortaInMillis / 1000);

                        mPausesCounter++;
                    }
                    else{
                        mTextViewEstado.setText("Descanso largo ");
                        mTimeLeftInMillis = pausaLargaInMillis;
                        mProgressBar.setMax((int)pausaLargaInMillis / 1000);
                        mPausesCounter = 1;
                    }
                }

                mIsCurrentlyInBreak = !mIsCurrentlyInBreak;
                updateCountDownText();
                updateDetailsText();
                startTimer();
            }
        }.start();

        mTimerRunning = true;

        hideCountDownStoppedButtons();
    }

    @Override
    protected void onStop(){
        super.onStop();

        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();

        editor.putLong("millisLeft", mTimeLeftInMillis);
        editor.putBoolean("timerRunning", mTimerRunning);
        editor.putLong("endTime", mEndTime);
        editor.putBoolean("setEdit",false);
        editor.putString("SesionStart", mTextViewSesionIniciada.toString());

        editor.apply();
        updateCountDownText();
        updateDetailsText();
    }

    @Override
    protected void onStart(){
        super.onStart();

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        if(prefs.getBoolean("setEdit", true)){
                setup();
                prefs.edit().putBoolean("setEdit",false);
                prefs.edit().apply();
        }
        else {
            mTimeLeftInMillis = prefs.getLong("millisLeft", sesionTrabajoInMillis);
            mTimerRunning = prefs.getBoolean("timerRunning", false);
            resume();
            updateCountDownText();
            updateDetailsText();

            if (mTimerRunning) {
                mEndTime = prefs.getLong("endTime", 0);
                mTimeLeftInMillis = mEndTime - System.currentTimeMillis();

                if (mTimeLeftInMillis < 0) {
                    mTimeLeftInMillis = 0;
                    pauseTimer();
                    updateCountDownText();
                    updateDetailsText();
                } else {
                    startTimer();
                }
            }
        }
    }

    private void pauseTimer(){
        mTimerRunning = false;
        if (mCountDownTimer != null) {
            mCountDownTimer.cancel();
        }
        showCountDownStoppedButtons();
    }

    private void resetTimer(){

        if(mIsCurrentlyInBreak){
            if(mPausesCounter <= LIMIT_SHORT_PAUSES){
                mTimeLeftInMillis = pausaCortaInMillis;
            }
            else{
                mTimeLeftInMillis = pausaLargaInMillis;
            }
        }
        else{
            mTimeLeftInMillis = sesionTrabajoInMillis;
        }

        mProgressBar.setProgress(0);
        updateCountDownText();
        updateDetailsText();
    }

    private void cleanTimer(){
        //Volver a estado inicial
        mTimerRunning = false;
        mIsCurrentlyInBreak = false;
        mPausesCounter = 1;
        mTextViewEstado.setText("Sesión de Trabajo");
        mTimeLeftInMillis = sesionTrabajoInMillis;
        mProgressBar.setProgress(0);

        updateCountDownText();
        updateDetailsText();
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

    private void updateDetailsText(){
        String formatted_time;
        SimpleDateFormat formatter = new SimpleDateFormat("HH:mm aa");
        formatter.setTimeZone(TimeZone.getTimeZone("GMT-5")); //Colombia Standard Time

            Date startTime = new Date();
            formatted_time = formatter.format(startTime);
            mTextViewSesionIniciada.setText(formatted_time);

        Date finaliza = new Date(new Date().getTime() + mTimeLeftInMillis);
        formatted_time = formatter.format(finaliza);

        mTextViewFinaliza.setText(formatted_time);
    }

    private void showCountDownStoppedButtons(){
        mButtonPause.setVisibility(View.INVISIBLE);
        mButtonStart.setVisibility(View.VISIBLE);
        mButtonReset.setVisibility(View.VISIBLE);
        mButtonClean.setVisibility(View.VISIBLE);
        mButtonConfig.setVisibility(View.VISIBLE);
    }

    private void hideCountDownStoppedButtons(){
        mButtonPause.setVisibility(View.VISIBLE);
        mButtonStart.setVisibility(View.INVISIBLE);
        mButtonReset.setVisibility(View.INVISIBLE);
        mButtonClean.setVisibility(View.INVISIBLE);
        mButtonConfig.setVisibility(View.INVISIBLE);
    }

    private void setup(){

        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        String sesionTrabajo = prefs.getString("SesionTrabajo", "20 minutos");
        String pausaCorta = prefs.getString("PausaCorta", "5 minutos");
        String pausaLarga = prefs.getString("PausaLarga", "10 minutos");



        //Formato: <minutos> <label que no nos interesa>
        // por eso split(" ")[0]
        sesionTrabajoInMillis = Integer.parseInt(sesionTrabajo.split(" ")[0]);
        pausaCortaInMillis = Integer.parseInt(pausaCorta.split(" ")[0]);
        pausaLargaInMillis = Integer.parseInt(pausaLarga.split(" ")[0]);

        // Minutos a Milisegundos
        sesionTrabajoInMillis *= 60000;
        pausaCortaInMillis *= 60000;
        pausaLargaInMillis *= 60000;

        mTimeLeftInMillis = sesionTrabajoInMillis;
        mProgressBar.setProgress(0);
        mProgressBar.setMax((int)sesionTrabajoInMillis / 1000);
        mProgressBar.setProgress(0);

        updateCountDownText();
        updateDetailsText();
    }

    private void resume(){
        SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);

        String sesionTrabajo = prefs.getString("SesionTrabajo", "20 minutos");
        String pausaCorta = prefs.getString("PausaCorta", "5 minutos");
        String pausaLarga = prefs.getString("PausaLarga", "10 minutos");
        //Formato: <minutos> <label que no nos interesa>
        // por eso split(" ")[0]
        sesionTrabajoInMillis = Integer.parseInt(sesionTrabajo.split(" ")[0]);
        pausaCortaInMillis = Integer.parseInt(pausaCorta.split(" ")[0]);
        pausaLargaInMillis = Integer.parseInt(pausaLarga.split(" ")[0]);

        // Minutos a Milisegundos
        sesionTrabajoInMillis *= 60000;
        pausaCortaInMillis *= 60000;
        pausaLargaInMillis *= 60000;
    }
}

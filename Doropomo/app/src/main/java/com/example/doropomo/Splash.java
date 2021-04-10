package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Splash.this, MainActivity.class);
                startActivity(intent);
                Splash.this.finish();
            }
        }, 3000);
    }
}
package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import com.example.doropomo.utilidades.Inicio;

import java.util.Timer;
import java.util.TimerTask;

public class Splash extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        /*
        if(((Inicio)this.getApplication()).getIniciada()==1){
            Toast.makeText(getBaseContext(), "Genial", Toast.LENGTH_LONG).show();
        }
        else{
            ((Inicio)this.getApplication()).setIniciada(1);
        }
        */
        //((Inicio)this.getApplication()).setIniciada(1);
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent;
                boolean timerRunning = false;

                    SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                    timerRunning = prefs.getBoolean("timerRunning", false);

                    if(timerRunning){
                        intent = new Intent(Splash.this, Temporizador.class);
                        prefs.edit().putBoolean("setEdit", false);
                        prefs.edit().apply();
                    }
                    else{
                        intent = new Intent(Splash.this, Bienvenida.class);
                    }

                    startActivity(intent);
                    Splash.this.finish();
            }
        }, 3000);
    }
}
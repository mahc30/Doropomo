package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import java.util.Timer;
import java.util.TimerTask;

public class ConfiguracionInicial extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_inicial);
    }

    public void onclickBtncontinuar(View view) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ConfiguracionInicial.this, Temporizador.class);
                startActivity(intent);
                ConfiguracionInicial.this.finish();
            }
        }, 0);
    }


}
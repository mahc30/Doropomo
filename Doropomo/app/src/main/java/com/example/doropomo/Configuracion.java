package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;

public class Configuracion extends AppCompatActivity {

    private Spinner mSpinnerSesion;
    private Spinner mSpinnerPausaCorta;
    private Spinner mSpinnerPausaLarga;

    private String lastSesionTrabajo;
    private String lastPausaCorta;
    private String lastPausaLarga;
    private String sesionTrabajo;
    private String pausaCorta;
    private String pausaLarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion);

        mSpinnerSesion = findViewById(R.id.spinnerSesionC);
        mSpinnerPausaCorta = findViewById(R.id.spinnerPausaCortaC);
        mSpinnerPausaLarga = findViewById(R.id.spinnerPausaLargaC);

        mSpinnerSesion.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sesionTrabajo = mSpinnerSesion.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                sesionTrabajo = "";
            }
        });

        mSpinnerPausaCorta.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pausaCorta = mSpinnerPausaCorta.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pausaCorta = "";
            }
        });

        mSpinnerPausaLarga.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                pausaLarga = mSpinnerPausaLarga.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                pausaLarga = "";
            }
        });
    }

    public void onClickAtras(View view) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Configuracion.this, Temporizador.class);
                startActivity(intent);
                Configuracion.this.finish();
            }
        }, 0);
    }

    public void onclickBtncontinuar(View view) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(Configuracion.this, Temporizador.class);

                SharedPreferences prefs = getSharedPreferences("prefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = prefs.edit();

                editor.putString("SesionTrabajo", sesionTrabajo);
                editor.putString("PausaCorta", pausaCorta);
                editor.putString("PausaLarga", pausaLarga);
                editor.putBoolean("setEdit", true);

                editor.apply();

                startActivity(intent);
                Configuracion.this.finish();
            }
        }, 0);
    }
}
package com.example.doropomo;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;

import java.util.Timer;
import java.util.TimerTask;

public class ConfiguracionInicial extends AppCompatActivity implements AdapterView.OnItemSelectedListener {

    private Spinner mSpinnerSesion;
    private Spinner mSpinnerPausaCorta;
    private Spinner mSpinnerPausaLarga;

    private String sesionTrabajo;
    private String pausaCorta;
    private String pausaLarga;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_configuracion_inicial);

        mSpinnerSesion = findViewById(R.id.spinnerSesion);
        mSpinnerPausaCorta = findViewById(R.id.spinnerPausaCorta);
        mSpinnerPausaLarga = findViewById(R.id.spinnerPausaLarga);

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

    public void onclickBtncontinuar(View view) {
        Timer t = new Timer();
        t.schedule(new TimerTask() {

            @Override
            public void run() {
                // TODO Auto-generated method stub
                Intent intent = new Intent(ConfiguracionInicial.this, Temporizador.class);
                intent.putExtra("SesionTrabajo", sesionTrabajo);
                intent.putExtra("PausaCorta", pausaCorta);
                intent.putExtra("PausaLarga", pausaLarga);
                startActivity(intent);
                ConfiguracionInicial.this.finish();
            }
        }, 0);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
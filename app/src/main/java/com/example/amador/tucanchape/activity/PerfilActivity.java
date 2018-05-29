package com.example.amador.tucanchape.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.amador.tucanchape.R;

import mehdi.sakout.fancybuttons.FancyButton;

public class PerfilActivity extends AppCompatActivity {

    private TextView name_perf;
    private FancyButton ver_hor;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_perfil);

        ver_hor = findViewById(R.id.btn_hor);


        ver_hor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        getIncomingIntent();


    }

    private void getIncomingIntent(){

        if(getIntent().hasExtra("nombre")&& getIntent().hasExtra("telefono1")
                && getIntent().hasExtra("telefono2")){

            String name = getIntent().getStringExtra("nombre");
        String telu = getIntent().getStringExtra("telefono1");
        String teld = getIntent().getStringExtra("telefono2");



            setPerfil(name, telu, teld);


        }
    }

    private void setPerfil(String nombre, String telu, String teld) {

        TextView name = findViewById(R.id.name_prof);
        TextView teuno = findViewById(R.id.tel_1_prof);
        TextView tedos = findViewById(R.id.tel_2_prof);

        name.setText(nombre);
        teuno.setText(telu);
        tedos.setText(teld);

    }

}

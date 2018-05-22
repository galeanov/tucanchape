package com.example.amador.tucanchape.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;

public class InicioSesionActivity extends AppCompatActivity {

    private ImageButton pel_user, admin;
    private AppPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        prefs = new AppPreferences(this);
        prefs.setSplash(true);

        pel_user = findViewById(R.id.btn_user);
        admin = findViewById(R.id.btn_admin);

        admin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(InicioSesionActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        });

        pel_user.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setUserAuth(true);
                prefs.setUserType("U");
                Intent i = new Intent(InicioSesionActivity.this, MenuUserActivity.class);
                startActivity(i);
                finish();
            }
        });
    }
}

package com.example.amador.tucanchape.activity;


import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;

public class SplashActivity extends AppCompatActivity {

    private final int REQUEST_CODE_ASK_PERMISSIONS = 1991;

    private ImageView ivn;
    private AppPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        requestPermisison();
    }

    public void init(){
        prefs = new AppPreferences(this);

        if(prefs!=null) {
            if (prefs.getUserAuth()) {
                if (prefs.getUserType().endsWith("A")) {
                    startActivity(new Intent(this, MenuAdminActivity.class));
                    finish();
                } else {
                    startActivity(new Intent(this, MenuUserActivity.class));
                    finish();
                }
                return;
            }
            if(prefs.getSplash()){
                startActivity(new Intent(this, InicioSesionActivity.class));
                finish();
                return;
            }

        }

        ivn = findViewById(R.id.ivlog);
        Animation myanim = AnimationUtils.loadAnimation(this,R.anim.mytransition);
        ivn.startAnimation(myanim);
        final Intent i = new Intent(this, SlideInicioActivity.class);
        Thread timer = new Thread() {
            public void run() {
                try {
                    sleep(5000);
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    startActivity(i);
                    finish();
                }
            }
        };
        timer.start();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (REQUEST_CODE_ASK_PERMISSIONS == requestCode) {
            if (grantResults[0] == PackageManager.PERMISSION_GRANTED)
                init();
            else {
                Toast.makeText(this, "Permiso denegado, debe conceder permisos de localización para el buen funcionamiento de la app", Toast.LENGTH_LONG).show();
                requestPermisison();
            }
        } else
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    private void requestPermisison() {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
            int hasAccountsPermission = checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
            if (hasAccountsPermission != PackageManager.PERMISSION_GRANTED)
                requestPermissions(new String[]{
                        Manifest.permission.ACCESS_FINE_LOCATION,
                        Manifest.permission.ACCESS_COARSE_LOCATION}, REQUEST_CODE_ASK_PERMISSIONS);
            else
                init();
        } else
            init();
    }
}

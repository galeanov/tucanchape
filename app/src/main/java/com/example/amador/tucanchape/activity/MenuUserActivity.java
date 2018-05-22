package com.example.amador.tucanchape.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.fragment.User1Fragment;
import com.example.amador.tucanchape.fragment.User2Fragment;
import com.example.amador.tucanchape.fragment.User3Fragment;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;
import com.google.firebase.auth.FirebaseAuth;


public class MenuUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AppPreferences prefs;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);

        prefs = new AppPreferences(this);
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_1:
                            selectedFragment = new User1Fragment();
                            break;
                        case R.id.nav_2:
                            selectedFragment = new User2Fragment();
                            break;
                        case R.id.nav_3:
                            selectedFragment = new User3Fragment();
                            //borrar despues
                            mAuth.signOut();
                            prefs.logout();
                            startActivity(new Intent(MenuUserActivity.this, InicioSesionActivity.class));
                            finish();
                            break;
                    }

                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return  true;
                }


            };
}

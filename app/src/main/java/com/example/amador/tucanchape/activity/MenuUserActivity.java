package com.example.amador.tucanchape.activity;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.fragment.CanchaReservaFragment;
import com.example.amador.tucanchape.fragment.HorariosReservaFragment;
import com.example.amador.tucanchape.fragment.PerfilFragment;
import com.example.amador.tucanchape.fragment.UserBuscarFragment;
import com.example.amador.tucanchape.fragment.UserCanchaFragment;
import com.example.amador.tucanchape.model.Empresa;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;


public class MenuUserActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private AppPreferences prefs;
    private UserCanchaFragment userCanchaFragment = UserCanchaFragment.newInstance();
    private UserBuscarFragment userBuscarFragment = null;


    @Override
    public void onBackPressed() {

        CanchaReservaFragment canchaReservaFragment = null;
        HorariosReservaFragment horariosReservaFragment = null;
        PerfilFragment perfilFragment = null;

        for (Fragment fragment : getSupportFragmentManager().getFragments()){
            if(fragment instanceof CanchaReservaFragment)
                canchaReservaFragment = (CanchaReservaFragment) fragment;

            if(fragment instanceof HorariosReservaFragment)
                horariosReservaFragment = (HorariosReservaFragment) fragment;

            if(fragment instanceof PerfilFragment)
                perfilFragment = (PerfilFragment) fragment;
        }

        if(horariosReservaFragment!=null){
            getSupportFragmentManager().beginTransaction().remove(horariosReservaFragment).commit();
            return;
        }

        if(canchaReservaFragment!=null){
            getSupportFragmentManager().beginTransaction().remove(canchaReservaFragment).commit();
            return;
        }

        if(perfilFragment!=null){
            getSupportFragmentManager().beginTransaction().remove(perfilFragment).commit();
            return;
        }

        super.onBackPressed();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_user);

        prefs = new AppPreferences(this);
        mAuth = FirebaseAuth.getInstance();

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        getSupportFragmentManager().beginTransaction().add(R.id.fragment_container, userCanchaFragment).commit();
    }

    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;

                    switch (item.getItemId()){
                        case R.id.nav_1:
                            selectedFragment = userCanchaFragment;
                            break;
                        case R.id.nav_2:
                            if(userBuscarFragment==null){
                                userBuscarFragment = UserBuscarFragment.newInstance();
                                getSupportFragmentManager().beginTransaction().hide(userCanchaFragment).add(R.id.fragment_container, userBuscarFragment).commit();
                                return true;
                            }
                            selectedFragment = userBuscarFragment;
                            break;
                        case R.id.nav_3:
                            //borrar despues
                            mAuth.signOut();
                            prefs.logout();
                            startActivity(new Intent(MenuUserActivity.this, InicioSesionActivity.class));
                            finish();
                            return true;
                    }

                    getSupportFragmentManager().beginTransaction().hide(userBuscarFragment).hide(userCanchaFragment).show(selectedFragment).commit();
                    return  true;
                }


            };
}

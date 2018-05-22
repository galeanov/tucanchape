package com.example.amador.tucanchape.activity;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.TextView;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.adapter.CanchaReservaAdapter;
import com.example.amador.tucanchape.adapter.SliderAdapter;
import com.example.amador.tucanchape.fragment.CanchaFragment;
import com.example.amador.tucanchape.fragment.CanchaReservaFragment;
import com.example.amador.tucanchape.fragment.EmpresaFragment;
import com.example.amador.tucanchape.fragment.HorariosFragment;
import com.example.amador.tucanchape.fragment.HorariosReservaFragment;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;
import com.facebook.login.LoginManager;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class MenuAdminActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private FirebaseAuth mAuth;
    private AppPreferences prefs;
    private Menu menu;
    private EmpresaFragment empresaFragment;
    private boolean menuVal = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu_admin);

        if(empresaFragment ==null)
            empresaFragment = EmpresaFragment.newInstance();

        prefs = new AppPreferences(this);

        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = findViewById(R.id.nav_view);
        final TextView name = navigationView.getHeaderView(0).findViewById(R.id.nameTextView);
        TextView email = navigationView.getHeaderView(0).findViewById(R.id.emailTextView);
        navigationView.setNavigationItemSelectedListener(this);

        mAuth = FirebaseAuth.getInstance();
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {
            name.setText(user.getDisplayName());
            email.setText(user.getEmail());
            DatabaseReference myRef = FirebaseDatabase.getInstance().getReference();
            myRef.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {
                    if(name.getText().toString().equals(""))
                        for (DataSnapshot postSnapshot: dataSnapshot.child("user").getChildren()) { //empresas
                            if(postSnapshot.child("correo").getValue(String.class).equals(user.getEmail())){
                                name.setText(postSnapshot.child("nombres").getValue(String.class) + " " + postSnapshot.child("apellidos").getValue(String.class));
                                break;
                            }
                        }
                    boolean validate = false;
                    for (DataSnapshot postSnapshot: dataSnapshot.child("empresa").getChildren()) { //empresas
                                if(postSnapshot.child("idusuario").getValue(String.class).equals(user.getUid())){
                                    validate = true;
                                    break;
                                }
                    }
                    if(!validate){
                        Intent intent = new Intent(MenuAdminActivity.this, RegisterEmpresaActivity.class);
                        startActivity(intent);
                        finish();
                    }
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {}
            });
        }else{
            goLoginScreen();
        }
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();
        manager.beginTransaction().replace(R.id.escenario, CanchaFragment.newInstance()).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            for(Fragment fragment : getSupportFragmentManager ().getFragments()){
                if(fragment instanceof HorariosFragment) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    return;
                }

                if(fragment instanceof HorariosReservaFragment) {
                    getSupportFragmentManager().beginTransaction().remove(fragment).commit();
                    return;
                }
            }
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_admin, menu);
        this.menu =  menu;
        menu.findItem(R.id.action_editar).setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_editar) {
            if(!menuVal){
                empresaFragment.Editar(true);
                item.setIcon(R.drawable.ic_save_black_24dp);
                menuVal=true;
            }else {
                empresaFragment.Salvar();
                item.setIcon(R.drawable.ic_edit_black_24dp);
                menuVal = false;
            }

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        android.support.v4.app.FragmentManager manager = getSupportFragmentManager();

        if (id == R.id.nav_per) {
            menu.findItem(R.id.action_editar).setVisible(true);
            manager.beginTransaction().replace(R.id.escenario, empresaFragment).commit();
        } else if (id == R.id.nav_canchas) {
            menu.findItem(R.id.action_editar).setVisible(false);
            manager.beginTransaction().replace(R.id.escenario, CanchaFragment.newInstance()).commit();
        } else if (id == R.id.nav_reserva) {
            menu.findItem(R.id.action_editar).setVisible(false);
            manager.beginTransaction().replace(R.id.escenario, CanchaReservaFragment.newInstance()).commit();
        }else if (id == R.id.nav_logout) {
            mAuth.signOut();
            prefs.logout();
            startActivity(new Intent(this, InicioSesionActivity.class));
            finish();
        }

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void goLoginScreen() {
        Intent intent = new Intent(this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    public void logout(View view) {
        FirebaseAuth.getInstance().signOut();
        LoginManager.getInstance().logOut();
        goLoginScreen();
    }
}

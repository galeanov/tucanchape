package com.example.amador.tucanchape.activity;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import com.example.amador.tucanchape.R;
import com.example.amador.tucanchape.sharedPreferences.AppPreferences;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rengwuxian.materialedittext.MaterialEditText;

import java.io.IOException;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

import mehdi.sakout.fancybuttons.FancyButton;

public class RegisterEmpresaActivity extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener{

    public static final int MY_PERMISSIONS_REQUEST =1;

    private MaterialEditText cancha, tel1, tel2;
    private FancyButton registrar_cancha;
    private TextView irmenu;
    private String uid;
    private FirebaseAuth mAuth;
    private AppPreferences prefs;
    private GoogleApiClient mGoogleApiClient;
    private Location location = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_empresa);

        prepareGeo();

        prefs = new AppPreferences(this);

        mAuth =FirebaseAuth.getInstance();

        cancha = findViewById(R.id.et_canc);
        tel1 = findViewById(R.id.et_tel);
        tel2 = findViewById(R.id.et_tel2);
        irmenu  = findViewById(R.id.ir_menu);
        registrar_cancha = findViewById(R.id.reg_can);

        irmenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setRegisterEmpresa(true);
                Intent intent1 = new Intent(RegisterEmpresaActivity.this, MenuAdminActivity.class);
                startActivity(intent1);
                finish();
            }
        });

        registrar_cancha.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prefs.setRegisterEmpresa(true);
                crearcancha();
                Intent intent = new Intent( RegisterEmpresaActivity.this, MenuAdminActivity.class);
                startActivity(intent);
                finish();
            }
        });

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            uid = user.getUid();
        }

    }

    private void prepareGeo() {
        if (mGoogleApiClient == null) {
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .addConnectionCallbacks(this)
                    .addOnConnectionFailedListener(this)
                    .addApi(LocationServices.API)
                    .build();
        }
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    private void crearcancha() {
        final String empr = cancha.getText().toString().trim();
        final String te1 = tel1.getText().toString().trim();
        final String te2 = tel2.getText().toString().trim();

        FirebaseUser user = mAuth.getCurrentUser();

        if (!TextUtils.isEmpty(empr) && !TextUtils.isEmpty(te1) && !TextUtils.isEmpty(te2)){
            //p
            DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("empresa");
            // id de cada cancha por usuario
            DatabaseReference currentUserDB = mDatabase.child(UUID.randomUUID().toString());
            currentUserDB.child("idusuario").setValue(uid);
            currentUserDB.child("nombre").setValue(empr);
            currentUserDB.child("telefono1").setValue(te1.toString());
            currentUserDB.child("telefono2").setValue(te2.toString());
            currentUserDB.child("userAdmin").setValue(user.getEmail());
            currentUserDB.child("lat").setValue(location!=null?location.getLatitude():0.0);
            currentUserDB.child("lng").setValue(location!=null?location.getLongitude():0.0);
            Toast.makeText(RegisterEmpresaActivity.this,"Exito al registrar tu empresa",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(RegisterEmpresaActivity.this,"Campos vacíos",Toast.LENGTH_SHORT).show();
        }

    }

    private void builAlertMessageNoGps() {
        final AlertDialog.Builder builder= new AlertDialog.Builder(RegisterEmpresaActivity.this);
        builder.setMessage("Su Gps esta desabilitado, desea habilitarlo?")
                .setCancelable(false)
                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    public void onClick(@SuppressWarnings("Unussed")final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id){
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                })
                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(final DialogInterface dialog, @SuppressWarnings ("Unussed") final int id) {
                        dialog.cancel();
                    }
                });
        final AlertDialog alert=builder.create();
        alert.show();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        LocationServices.getFusedLocationProviderClient(this).getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if(location!=null){
                            RegisterEmpresaActivity.this.location = location;
                        }else
                            builAlertMessageNoGps();
                    }
                });
    }

    @Override
    public void onConnectionSuspended(int i) {
        builAlertMessageNoGps();
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        builAlertMessageNoGps();
    }
}
